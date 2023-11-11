package org.launchcode.techjobs.persistent.controllers;
import jakarta.validation.Valid;
import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.launchcode.techjobs.persistent.models.Job;

import java.util.Optional;

@Controller
@RequestMapping("skills")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;

    // add index method to find all skills in the database
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("skills", skillRepository.findAll());
        return "skills/index";
    }


    @GetMapping("add")
    public String displayAddSkillsForm(Model model) {
        model.addAttribute(new Skill());
        return "skills/add";
    }

    // add code to save a valid object if skills object info is valid
    @PostMapping("add")
    public String processAddSkillForm(@ModelAttribute @Valid Skill newSkills,
                                         Errors errors, Model model) {

        if (errors.hasErrors()) {
            return "skills/add";
        } else {
            skillRepository.save(newSkills);
            return "redirect:";
        }
    }

    @GetMapping("view/{skillsId}")
    public String displayViewSkill(Model model, @PathVariable int skillsId) {

        Optional optSkill = skillRepository.findById(skillsId);
        if (optSkill.isPresent()) {
            Skill skill = (Skill) optSkill.get();
            model.addAttribute("skill", skill);
            return "skill/view";
        } else {
            return "redirect:../";
        }

    }

}
