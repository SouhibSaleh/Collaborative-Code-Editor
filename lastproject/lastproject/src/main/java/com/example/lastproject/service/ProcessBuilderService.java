package com.example.lastproject.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ProcessBuilderService {

    public String runCode(String programmingLanguage,String code,String []args)
    {
        switch (programmingLanguage){
            case "c++":return runCppCode(code,args);
            default: return "language doesn't exist";
        }
    }
    public String runCppCode(String code,String[]arg) {
        File tempDir = null;
        try {
            tempDir = Files.createTempDirectory("cppcode").toFile();

            File cppFile = new File(tempDir, "main.cpp");
            try (FileWriter writer = new FileWriter(cppFile)) {
                writer.write(code);
            }

            ProcessBuilder compileBuilder = new ProcessBuilder("g++", "main.cpp", "-o", "main");
            compileBuilder.directory(tempDir);
            compileBuilder.redirectErrorStream(true);
            Process compileProcess = compileBuilder.start();

            BufferedReader compileReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
            StringBuilder compileOutput = new StringBuilder();
            String line;
            while ((line = compileReader.readLine()) != null) {
                compileOutput.append(line).append("\n");
            }

            int compileStatus = compileProcess.waitFor();
            if (compileStatus != 0) {
                return "Compilation failed:\n" + compileOutput;
            }

            // Step 2: Run the compiled binary
            ProcessBuilder runBuilder = new ProcessBuilder("./main");
            runBuilder.directory(tempDir);
            Process runProcess = runBuilder.start();

            // Send input (simulating cin >>)
            if (arg != null && arg.length > 0) {
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
                    for (String input : arg) {
                        writer.write(input);
                        writer.newLine();
                    }
                    writer.flush();
                }
            }

            // Read output from the program
            BufferedReader runReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            StringBuilder runOutput = new StringBuilder();
            while ((line = runReader.readLine()) != null) {
                runOutput.append(line).append("\n");
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                runOutput.append("ERROR: ").append(line).append("\n");
            }

            runProcess.waitFor();
            return runOutput.toString();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            if (tempDir != null) {
                Arrays.stream(tempDir.listFiles()).forEach(File::delete);
                tempDir.delete();
            }
        }
    }


}
