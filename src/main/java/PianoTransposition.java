import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author ashif ismail
 * {@link https://github.com/ashif-ismail Github}
 */

public class PianoTransposition {

    private static final int OCTAVE_MIN = -3;
    private static final int OCTAVE_MAX = 5;
    private static final int NOTES_PER_OCTAVE = 12;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("[-] Usage error: java -jar task.jar <inputFile> <semitone> <outputFile>");
            System.exit(1);
        }

        String inputFile = args[0];
        int semitone = Integer.parseInt(args[1]);
        String outputFile = args[2];

        try {
            System.out.println("[-] Starting Transposition for given input file with semitone value " + semitone);
            List<int[]> notes = readNotesFromFile(inputFile);
            List<int[]> transposedNotes = transposeNotes(notes, semitone);
            writeNotesToFile(transposedNotes, outputFile);
        } catch (Exception e) {
            System.err.println("[-] Error : " + e.getMessage());
            System.exit(1);
        }
    }

    private static List<int[]> readNotesFromFile(String inputFile) throws IOException {
        Gson gson = new Gson();
        System.out.println("[-] Reading input file from " + inputFile);
        try (FileReader reader = new FileReader(inputFile)) {
            Type noteListType = new TypeToken<List<int[]>>() {}.getType();
            return gson.fromJson(reader, noteListType);
        }
    }

    private static void writeNotesToFile(List<int[]> notes, String outputFile) throws IOException {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(outputFile)) {
            gson.toJson(notes, writer);
        }
        System.out.println("[-] Successfully completed the transposition and output is written to " + outputFile);
    }

    private static List<int[]> transposeNotes(List<int[]> notes, int semitone) {
        for (int[] note : notes) {
            int octave = note[0];
            int noteNumber = note[1];

            int newNoteNumber = noteNumber + semitone;
            int octaveChange = 0;

            while (newNoteNumber < 1) {
                newNoteNumber += NOTES_PER_OCTAVE;
                octaveChange -= 1;
            }

            while (newNoteNumber > NOTES_PER_OCTAVE) {
                newNoteNumber -= NOTES_PER_OCTAVE;
                octaveChange += 1;
            }

            note[0] = octave + octaveChange;
            note[1] = newNoteNumber;

            if (note[0] < OCTAVE_MIN || note[0] > OCTAVE_MAX) {
                throw new IllegalArgumentException("[-] Note out of keyboard range: " + note[0] + ", " + note[1]);
            }
        }
        return notes;
    }
}
