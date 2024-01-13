package io.demo;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DataController {

    private final JobLauncher jobLauncher;
    private final Job myJob;
    private final VisualizationService visualizationService;

    @Autowired
    public DataController(JobLauncher jobLauncher, @Qualifier("myJob") Job myJob, VisualizationService visualizationService) {
        this.jobLauncher = jobLauncher;
        this.myJob = myJob;
        this.visualizationService = visualizationService;
    }

    @GetMapping("/processData")
    public String processData() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(myJob, jobParameters);

            // Log job execution details
            logJobExecutionDetails(jobExecution);

            // Additional logic related to your Visualization Dashboard
            visualizationService.updateVisualizationData();

            return "Data processing and visualization update initiated successfully. Job Execution ID: " + jobExecution.getId();
        } catch (Exception e) {
            return "Error processing data: " + e.getMessage();
        }
    }

    private void logJobExecutionDetails(JobExecution jobExecution) {
        // Log job execution details such as start time, end time, and status
        System.out.println("Job Execution ID: " + jobExecution.getId());
        System.out.println("Start Time: " + jobExecution.getStartTime());
        System.out.println("End Time: " + jobExecution.getEndTime());
        System.out.println("Status: " + jobExecution.getStatus());
    }
}
