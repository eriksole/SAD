import java.io.*;
import java.util.LinkedList;

import static java.awt.event.KeyEvent.*;

class TestReadLine {
  public static void main(String[] args) {
    BufferedReader in = new EditableBufferedReader(
      new InputStreamReader(System.in));
    String str = null;
    try {
      str = in.readLine();
    } catch (IOException e) { e.printStackTrace(); }
    System.out.println("\nline is: " + str);
  }
}

class EditableBufferedReader extends BufferedReader {

    private  String ttyConfig;
    private  Line line = new Line("");
    private  Text text = new Text();
    private Reader in;
    private Console view = new Console();

    //Constants de les possibles comandes
    private int UP_KEY = 65, DOWN_KEY = 66, RIGHT_KEY = 67, LEFT_KEY = 68, HOME = 72, END = 70, ENDL = '\n';

    public EditableBufferedReader(Reader reader) {
        super(reader);
        in = reader;
    }

    public String readLine() {

        try {
            setTerminalToCBreak();

            int cursor_x = 1, cursor_y = 1;
            System.out.print("\033[2J\033[1;1H");
            while (true) {
                int key = in.read();
                if(key == 112) break;
                if(key == 91 || key == 27) continue;
                if(key == VK_DELETE) { //Tecla borrar
                    text.delete(cursor_x, cursor_y);
                    if (cursor_x > 1) cursor_x--;
                }
                else if(key == LEFT_KEY) { //Fletja esquerre
                    if (cursor_x > 1) cursor_x--;
                }
                else if(key == RIGHT_KEY) { //Fletja dreta
                    if (cursor_x <= text.lines[cursor_y-1].str.length()) cursor_x++;
                }
                else if(key == UP_KEY) {//Fletja adalt
                    if(cursor_y > 1) {
                        cursor_y--;
                        cursor_x = Math.min(cursor_x, text.lines[cursor_y-1].str.length()+1);
                    }
                }
                else if(key == DOWN_KEY) {//Fletja adalt
                    if(cursor_y < text.lines.length) {
                        cursor_y++;
                        cursor_x = Math.min(cursor_x, text.lines[cursor_y-1].str.length()+1);
                    }
                }
                else if(key == HOME) cursor_x = 1; //Tecla H
                else if(key == END) cursor_x = text.lines[cursor_y-1].str.length() + 1; //Tecla F
                else if(key == ENDL) {
                    text.endl(cursor_x, cursor_y);
                    cursor_y++;
                    cursor_x = 1;
                }
                else {
                    text.addChar(key, cursor_x, cursor_y);
                    cursor_x++;
                }
                //Refresquem la pantalla
                //System.out.print(key);
                view.updateView(text, cursor_x, cursor_y);
            }
            System.out.print("\033[1E\033[2K\033[3;1H");

            return line.str;
        }
        catch (IOException e) {
            System.err.println("IOException");
        }
        catch (InterruptedException e) {
            System.err.println("InterruptedException");
        }
        finally {
            try {
                stty( ttyConfig.trim() );
            }
            catch (Exception e) {
                System.err.println("Exception restoring tty config");
            }
        }
        return "";
    }

    private void setTerminalToCBreak() throws IOException, InterruptedException {

        ttyConfig = stty("-g");
        stty("-icanon min 1");
        stty("-echo");

    }

    //Executem les comandes a la terminal
    private String stty(final String args)
            throws IOException, InterruptedException {
        String cmd = "stty " + args + " < /dev/tty";

        return exec(new String[] {
                "sh",
                "-c",
                cmd
        });
    }

    private String exec(final String[] cmd)
            throws IOException, InterruptedException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        Process p = Runtime.getRuntime().exec(cmd);
        int c;
        InputStream in = p.getInputStream();

        while ((c = in.read()) != -1) {
            bout.write(c);
        }

        in = p.getErrorStream();

        while ((c = in.read()) != -1) {
            bout.write(c);
        }

        p.waitFor();

        String result = new String(bout.toByteArray());
        return result;
    }

}

class Console {

    public void updateView(Text text, int cursor_x, int cursor_y) {
        //Borrem la linea, ens situem a la primera posicio
        System.out.print("\033[2J\033[1;1H");
        //Escrivim la linea
        for(Line line : text.lines) {
            System.out.println(line.str);
        }
        //Ens situem a la posició de cursor_x anterior
        System.out.print("\033["+cursor_y+";"+cursor_x+"H");
    }

}

class Text{

    public Line[] lines = new Line[1];

    public Text() {
        lines[0] = new Line("");
    }

    private static Line[] insert(Line[] a, Line key, int index)
    {
        Line[] result = new Line[a.length + 1];
        for (int i = 0; i < index; i++) result[i] = a[i];
        result[index] = key;
        for (int i = index + 1; i <= a.length; i++) result[i] = a[i - 1];

        return result;
    }

    public void endl(int cursor_x, int cursor_y) {
        String sentence_end = "";
        if(cursor_x <= lines[cursor_y-1].str.length()) sentence_end = lines[cursor_y-1].str.substring(cursor_x-1, lines[cursor_y-1].str.length());
        lines[cursor_y-1].str = lines[cursor_y-1].str.substring(0, cursor_x-1);
        lines = insert(lines, new Line(sentence_end), cursor_y);
    }

    public void delete(int cursor_x, int cursor_y) {
        lines[cursor_y-1].delete(cursor_x);
    }

    public void addChar(int c, int cursor_x, int cursor_y) {
        lines[cursor_y-1].addChar(c, cursor_x);
    }

}

class Line {

    public String str;

    public Line(String s) {
        str = s;
    }

    public String delete(int cursor_x) {
        if(cursor_x > 1) str = str.substring(0, cursor_x - 2) + str.substring(cursor_x - 1);
        return str;
    }

    public String addChar(int c, int cursor_x) {
        str = str.substring(0, cursor_x - 1) + (char)c + str.substring(cursor_x - 1);
        return str;
    }

}
