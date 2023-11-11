package org.launchcode.techjobs.persistent.controllers;

import jakarta.validation.Valid;
import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobRepository jobRepository;

    @RequestMapping("/")
    public String index(Model model) {

        model.addAttribute("title", "MyJobs");
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute("jobs", jobRepository.findAll());

        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                    Errors errors, Model model, @RequestParam int employerId, @RequestParam List<Integer> skills) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            return "add";
        } else {
            //if no errors, select the employer object that has been chosen
            // to be affiliated with the new job

            // check if employer exists in the database
            Optional <Employer> selectedEmployerOpt = employerRepository.findById(employerId);

            //checks if employer is present in the repository (intelliJ replaced null)
            if (selectedEmployerOpt.isPresent()) {
                Employer selectedEmployer = selectedEmployerOpt.get();
                model.addAttribute("employers", selectedEmployer);

                // if not null, takes it from optional & affiliate with new Job
                newJob.setEmployer(selectedEmployer);

                //save new job to Repository
                jobRepository.save(newJob);

                // get the skills data from a list of ids
                if (skills != null) {
                    List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
                    newJob.setSkills(skillObjs);
                }
            }
        }


        return "redirect:";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {


        Optional optJob = jobRepository.findById(jobId);
        if (optJob.isPresent()) {
            Job job= (Job) optJob.get();
            model.addAttribute("job", job);
            return "job/view";
        } else {
            return "redirect:../";
        }
    }

}
