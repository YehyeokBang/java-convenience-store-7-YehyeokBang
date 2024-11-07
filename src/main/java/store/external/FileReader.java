package store.external;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class FileReader<T> {

    private static final String FILE_ERROR_MESSAGE = "[ERROR] 파일(%s)을 불러오는 중 문제가 발생했습니다.";
    protected static final String SEPARATOR = ",";

    protected abstract String getFileAddress();
    protected abstract T parseLine(String line);

    public List<T> readAll() {
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(getFileAddress()))) {
            skipHeaderLine(reader);
            return loadData(reader);
        } catch (IOException e) {
            String message = String.format(FILE_ERROR_MESSAGE, getFileAddress());
            throw new IllegalStateException();
        }
    }

    private void skipHeaderLine(BufferedReader reader) throws IOException {
        reader.readLine();
    }

    private List<T> loadData(BufferedReader reader) throws IOException {
        List<T> data = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            data.add(parseLine(line));
        }
        return data;
    }
}