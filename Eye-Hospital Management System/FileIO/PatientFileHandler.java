package FileIO;

import Entity.Patient;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PatientFileHandler {
    private static final String FILE_PATH = "FileIO/patient.txt";
    private static final String DIRECTORY = "FileIO";

    public PatientFileHandler() {
        try {
            File directory = new File(DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error creating file or directory: " + e.getMessage());
        }
    }

    public void savePatient(Patient patient) throws IOException {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }

        List<Patient> patients = loadAllPatients();
        
        for (Patient p : patients) {
            if (p.getSerialNumber().equals(patient.getSerialNumber())) {
                throw new IOException("Patient with serial number " + patient.getSerialNumber() + " already exists!");
            }
        }
        
        patients.add(patient);
        saveAllPatients(patients);
    }

    public List<Patient> loadAllPatients() {
        List<Patient> patients = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(FILE_PATH), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 3) {
                        String serialNumber = parts[0].trim();
                        String name = parts[1].trim();
                        int age = Integer.parseInt(parts[2].trim());
                        patients.add(new Patient(serialNumber, name, age));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Patient file not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading patient file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing patient age: " + e.getMessage());
        }
        
        return patients;
    }

    private void saveAllPatients(List<Patient> patients) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8))) {
            for (Patient patient : patients) {
                writer.write(patient.getSerialNumber() + "|" + 
                           patient.getName() + "|" + 
                           patient.getAge());
                writer.newLine();
            }
        }
    }

    public void updatePatient(String serialNumber, Patient updatedPatient) throws IOException {
        if (updatedPatient == null) {
            throw new IllegalArgumentException("Updated patient cannot be null");
        }

        List<Patient> patients = loadAllPatients();
        boolean found = false;

        if (!serialNumber.equals(updatedPatient.getSerialNumber())) {
            for (Patient p : patients) {
                if (p.getSerialNumber().equals(updatedPatient.getSerialNumber())) {
                    throw new IOException("Patient with serial number " + 
                                        updatedPatient.getSerialNumber() + " already exists!");
                }
            }
        }

        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getSerialNumber().equals(serialNumber)) {
                patients.set(i, updatedPatient);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IOException("Patient with serial number " + serialNumber + " not found!");
        }

        saveAllPatients(patients);
    }

    public void removePatient(String serialNumber) throws IOException {
        List<Patient> patients = loadAllPatients();
        boolean removed = patients.removeIf(p -> p.getSerialNumber().equals(serialNumber));

        if (!removed) {
            throw new IOException("Patient with serial number " + serialNumber + " not found!");
        }

        saveAllPatients(patients);
    }

    public Patient findPatient(String serialNumber) {
        List<Patient> patients = loadAllPatients();
        
        for (Patient patient : patients) {
            if (patient.getSerialNumber().equals(serialNumber)) {
                return patient;
            }
        }
        
        return null;
    }

    public void displayAllPatients() {
        List<Patient> patients = loadAllPatients();
        
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }

        System.out.println("     Patient List     ");
        for (Patient patient : patients) {
            System.out.println(patient);
        }
    }
}