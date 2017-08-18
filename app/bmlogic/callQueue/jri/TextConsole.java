package bmlogic.callQueue.jri;

/**
 * Created by liwei on 2017/6/27.
 */

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TextConsole implements RMainLoopCallbacks {

    @Override
    public void rWriteConsole(Rengine re, String text, int oType) {
        System.out.print(text);
    }

    @Override
    public void rBusy(Rengine re, int which) {
        System.out.println("rBusy(" + which + ")");
    }

    @Override
    public String rReadConsole(Rengine re, String prompt, int addToHistory) {
        System.out.print(prompt);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String s = br.readLine();
            return (s == null || s.length() == 0) ? s : s + "\n";
        } catch (Exception e) {
            System.out.println("jriReadConsole exception: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void rShowMessage(Rengine re, String message) {
        System.out.println("rShowMessage \"" + message + "\"");
    }

    @Override
    public String rChooseFile(Rengine re, int newFile) {
        return "";
    }

    public void rFlushConsole(Rengine re) {
    }

    public void rLoadHistory(Rengine re, String filename) {
    }

    public void rSaveHistory(Rengine re, String filename) {
    }
}