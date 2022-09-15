import java.io.*;

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
    private  Line line = new Line();
    private Reader in;
    private Console view = new Console();

    //Constants de les possibles comandes
    private int LEFT_KEY = 68, RIGHT_KEY = 67, HOME = 72, END = 70;

    public EditableBufferedReader(Reader reader) {
        super(reader);
        in = reader;
    }

    public String readLine() {

        try {
            setTerminalToCBreak();
            int cursor = 1;
            System.out.print("\033[2K\033[2;1H");
            while (true) {
                int key = in.read();
                if(key == '\n') break;
                if(key == 91 || key == 27) continue;
                if(key == VK_DELETE) { //Tecla borrar
                    line.delete(cursor);
                    if (cursor > 1) cursor--;
                }
                else if(key == LEFT_KEY) { //Fletja esquerre
                    if (cursor > 1) cursor--;
                }
                else if(key == RIGHT_KEY) { //Fletja dreta
                    if (cursor <= line.str.length()) cursor++;
                }
                else if(key == HOME) cursor = 1; //Tecla H
                else if(key == END) cursor = line.str.length() + 1; //Tecla F
                else {
                    line.addChar(key, cursor);
                    cursor++;
                }
                //Refresquem la pantalla
                view.updateView(line, cursor);
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

    public void updateView(Line line, int cursor) {
        //Borrem la linea, ens situem a la primera posicio
        System.out.print("\033[2K\033[2;1H");
        //Escrivim la linea
        System.out.print(line.str);
        //Ens situem a la posició de cursor anterior
        System.out.print("\033[2;"+ cursor + "H");
    }

}

class Line {

    public String str;

    public Line() {
        str = "";
    }

    public String delete(int cursor) {
        if(cursor > 1) str = str.substring(0, cursor - 2) + str.substring(cursor - 1);
        return str;
    }

    public String addChar(int c, int cursor) {
        str = str.substring(0, cursor - 1) + (char)c + str.substring(cursor - 1);
        return str;
    }

}
