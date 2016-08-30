package board;

import com.sun.xml.internal.ws.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Parser {

    public static long[] stringToBitboard(String string) {
        long[] bitmaps = new long[GameState.NUMBER_OF_BITMAPS];
        long currentPosition = 1L;
        for (int i = string.length() - 1; i >= 0; i--) {
            int val = Character.getNumericValue(string.charAt(i));
            if(val > 0) {
                bitmaps[val] |= currentPosition;
                bitmaps[0] |= currentPosition;
            }
            currentPosition <<= 1;
        }
        return bitmaps;
    }

    public static String bitboardToString(long[] bitboards) {
        String string = "";
        // start at position 49
        long position = 0x1000000000000L;
        for (int i = 0; i < GameState.NUMBER_OF_FIELDS; i++) {
            for (int j = 1; j < 4; j++) {
                if ((position & bitboards[j]) != 0) {
                    string += j;
                    break;
                }
            }
            if(string.length() < (i + 1)) {
                string += "0";
            }
            position >>= 1;
        }
        return string;
    }

    static char[][] bitboardsToArray(GameState state) {
        char[][] board = new char[7][7];
        long position = 0x1000000000000L;
        for (int i = 0; i < GameState.NUMBER_OF_FIELDS; i++) {
            if (state.isOnPosition(position, 1)) {
                board[i / 7][i % 7] = 'R';
            } else if (state.isOnPosition(position, 2)) {
                board[i / 7][i % 7] = 'W';
            } else if (state.isOnPosition(position, 3)) {
                board[i / 7][i % 7] = 'B';
            } else {
                board[i / 7][i % 7] = '0';
            }
            position >>>= 1;
        }
        return board;
    }

    public static int[][] stringToArray(String string) {
        int[][] board = new int[7][7];
        if (string.length() != 49) {
            System.out.println("String incorrect");
            return null;
        }
        for (int i = 0; i < GameState.NUMBER_OF_FIELDS; i++) {
            board[i / 7][i % 7] =  Character.getNumericValue(string.charAt(i));
        }
        return board;
    }

    public static String moveToString(Move move) {
        String string = positionToString(move.positionFrom) + "-" + positionToString(move.positionTo);
        string += (move.isCaptureMove ? "x" : "");
        return string;
    }

    private static String positionToString(long position) {
        String string = "";
        switch((Long.numberOfLeadingZeros(position) - 15) % 7){
            case 0: string += "a";break;
            case 1: string += "b";break;
            case 2: string += "c";break;
            case 3: string += "d";break;
            case 4: string += "e";break;
            case 5: string += "f";break;
            case 6: string += "g";break;
            default: string += "ERROR";
        }
        string+= ((Long.numberOfLeadingZeros(position) - 15) / 7 + 1);
        return string;
    }


    public static String[][] fileToStringArray(File file) {
        String[][] arr;
        LinkedList<String[]> list = new LinkedList<>();
        try {
            String delimiter = ";";
            Scanner scanner = new Scanner(file);
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(delimiter);
                list.add(tokens);
            }
            arr = new String[list.size()][6];
            for(int j = 0; j < list.size(); j++) {
                for (int i = 0; i < list.get(j).length; i++) {
                    arr[j][i] = list.get(j)[i];
                }
            }
            scanner.close();
            return arr;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
