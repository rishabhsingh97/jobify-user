package com.rsuniverse.jobify_user.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rsuniverse.jobify_user.models.dtos.JobSeekerDTO;
import com.rsuniverse.jobify_user.models.entities.JobSeeker;
import com.rsuniverse.jobify_user.models.responses.PaginatedRes;
import com.rsuniverse.jobify_user.repos.JobSeekerRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobSeekerService {

    private final JobSeekerRepo jobSeekerRepo;
    private final ModelMapper modelMapper;

    public PaginatedRes<JobSeekerDTO> getAllJobSeekers(int page, int size) {
        log.info("Fetching all jobSeekers - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page - 1, size); // 1 index to 0 index
        Page<JobSeeker> jobSeekerPage = jobSeekerRepo.findAll(pageable);

        List<JobSeekerDTO> jobSeekers = jobSeekerPage.getContent().stream()
                .map(jobSeeker -> modelMapper.map(jobSeeker, JobSeekerDTO.class))
                .collect(Collectors.toList());

        return PaginatedRes.<JobSeekerDTO>builder()
                .items(jobSeekers)
                .pageNum(jobSeekerPage.getNumber() + 1) // 0 index to 1 index
                .pageSize(jobSeekerPage.getSize())
                .totalPages(jobSeekerPage.getTotalPages())
                .totalCount(jobSeekerPage.getTotalElements())
                .build();
    }

    public JobSeekerDTO getJobSeekerById(String id) {
        log.info("Fetching jobSeeker by id: {}", id);
        JobSeeker jobSeeker = jobSeekerRepo.findById(id).orElseThrow(()
                -> new RuntimeException("JobSeeker not found with id: " + id));
        return modelMapper.map(jobSeeker, JobSeekerDTO.class);
    }

    public JobSeekerDTO createJobSeeker(JobSeekerDTO jobSeekerDTO) {
        log.info("Creating jobSeeker: {}", jobSeekerDTO);
        JobSeeker jobSeeker = modelMapper.map(jobSeekerDTO, JobSeeker.class);
        jobSeekerRepo.save(jobSeeker);
        log.info("JobSeeker created successfully: {}", jobSeeker);
        return modelMapper.map(jobSeeker, JobSeekerDTO.class);
    }

    public JobSeekerDTO updateJobSeeker(String id, JobSeekerDTO jobSeekerDTO) {
        log.info("Updating jobSeeker with id: {}", id);
        JobSeeker jobSeeker = jobSeekerRepo.findById(id).orElseThrow(()
                -> new RuntimeException("JobSeeker not found with id: " + id));
        modelMapper.map(jobSeekerDTO, jobSeeker); 
        jobSeekerRepo.save(jobSeeker);
        log.info("JobSeeker updated successfully: {}", jobSeeker);
        return modelMapper.map(jobSeeker, JobSeekerDTO.class);
    }

    public void deleteJobSeeker(String id) {
        log.info("Deleting jobSeeker with id: {}", id);
        if (jobSeekerRepo.existsById(id)) {
            jobSeekerRepo.deleteById(id);
            log.info("JobSeeker with id {} deleted successfully", id);
        } else {
            log.warn("JobSeeker with id {} not found", id);
        }
    }
}
