package com.rsuniverse.jobify_user.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rsuniverse.jobify_user.models.dtos.JobSeekerDTO;
import com.rsuniverse.jobify_user.models.responses.BaseRes;
import com.rsuniverse.jobify_user.models.responses.PaginatedRes;
import com.rsuniverse.jobify_user.services.JobSeekerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/job-seekers")
@RequiredArgsConstructor
public class JobSeekerController {

    private final JobSeekerService jobSeekerService;

    /**
     *
     * @param page - pagination with page number starting with  index 1, defaults to 1
     * @param size - pagination with fixed page size, defaults to 10
     * @return Paginated list of JobSeekers
     */
    @GetMapping("")
    public ResponseEntity<BaseRes<PaginatedRes<JobSeekerDTO>>> getAllJobSeekers(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Incoming request to get all jobSeekers");
            PaginatedRes<JobSeekerDTO> paginatedRes = jobSeekerService.getAllJobSeekers(page, size);
            return BaseRes.<PaginatedRes<JobSeekerDTO>>success(paginatedRes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseRes<JobSeekerDTO>> getJobSeekerById(@PathVariable String id) {
        log.info("Incoming request to get jobSeeker with id: {}", id);
            JobSeekerDTO jobSeekerDTO = jobSeekerService.getJobSeekerById(id);
            return BaseRes.success(jobSeekerDTO);
    }

    @PostMapping("")
    public ResponseEntity<BaseRes<JobSeekerDTO>> createJobSeeker(@RequestBody @Valid JobSeekerDTO jobSeekerDTO) {
        log.info("Incoming request to create jobSeeker: {}", jobSeekerDTO);
            JobSeekerDTO createdJobSeeker = jobSeekerService.createJobSeeker(jobSeekerDTO);
            return BaseRes.success(createdJobSeeker);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseRes<JobSeekerDTO>> updateJobSeeker(@PathVariable String id, @RequestBody @Valid JobSeekerDTO jobSeekerDTO) {
        log.info("Incoming request to update jobSeeker with id: {}, jobSeeker: {}", id, jobSeekerDTO);
            JobSeekerDTO updatedJobSeeker = jobSeekerService.updateJobSeeker(id, jobSeekerDTO);
            return BaseRes.success(updatedJobSeeker);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseRes<Void>> deleteJobSeeker(@PathVariable String id) {
        log.info("Incoming request to delete jobSeeker with id: {}", id);
            jobSeekerService.deleteJobSeeker(id);
            return BaseRes.success(null);
    }
}
