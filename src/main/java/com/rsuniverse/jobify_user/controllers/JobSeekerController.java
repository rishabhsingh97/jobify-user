package com.rsuniverse.jobify_user.controllers;

import com.rsuniverse.jobify_user.services.CandidateService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rsuniverse.jobify_user.models.dtos.CandidateDTO;
import com.rsuniverse.jobify_user.models.responses.BaseRes;
import com.rsuniverse.jobify_user.models.responses.PaginatedRes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/job-seekers")
@RequiredArgsConstructor
public class JobSeekerController {

    private final CandidateService jobSeekerService;

    /**
     *
     * @param page - pagination with page number starting with  index 1, defaults to 1
     * @param size - pagination with fixed page size, defaults to 10
     * @return Paginated list of JobSeekers
     */
    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping("")
    public ResponseEntity<BaseRes<PaginatedRes<CandidateDTO>>> getAllJobSeekers(@RequestParam(defaultValue = "1") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Incoming request to get all jobSeekers");
            PaginatedRes<CandidateDTO> paginatedRes = jobSeekerService.getAllCandidates(page, size);
            return BaseRes.<PaginatedRes<CandidateDTO>>success(paginatedRes);
    }

    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping("/{id}")
    public ResponseEntity<BaseRes<CandidateDTO>> getJobSeekerById(@PathVariable String id) {
        log.info("Incoming request to get jobSeeker with id: {}", id);
            CandidateDTO jobSeekerDTO = jobSeekerService.getCandidateById(id);
            return BaseRes.success(jobSeekerDTO);
    }

    @RateLimiter(name = "defaultRateLimiter")
    @PostMapping("")
    public ResponseEntity<BaseRes<CandidateDTO>> createJobSeeker(@RequestBody @Valid CandidateDTO jobSeekerDTO) {
        log.info("Incoming request to create jobSeeker: {}", jobSeekerDTO);
            CandidateDTO createdJobSeeker = jobSeekerService.createCandidate(jobSeekerDTO);
            return BaseRes.success(createdJobSeeker);
    }

    @RateLimiter(name = "defaultRateLimiter")
    @PutMapping("/{id}")
    public ResponseEntity<BaseRes<CandidateDTO>> updateJobSeeker(@PathVariable String id, @RequestBody @Valid CandidateDTO jobSeekerDTO) {
        log.info("Incoming request to update jobSeeker with id: {}, jobSeeker: {}", id, jobSeekerDTO);
            CandidateDTO updatedJobSeeker = jobSeekerService.updateCandidate(id, jobSeekerDTO);
            return BaseRes.success(updatedJobSeeker);
    }

    @RateLimiter(name = "defaultRateLimiter")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseRes<Void>> deleteJobSeeker(@PathVariable String id) {
        log.info("Incoming request to delete jobSeeker with id: {}", id);
            jobSeekerService.deleteCandidate(id);
            return BaseRes.success(null);
    }
}
