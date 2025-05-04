package com.enotes.controller;

import com.enotes.entity.Notes;
import com.enotes.entity.User;
import com.enotes.repository.UserRepository;
import com.enotes.service.NotesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private NotesService notesService;




    @ModelAttribute
    public User getUser(Principal p, Model m){
       String email =  p.getName();
       User user = userRepo.findByEmail(email);
       m.addAttribute("user",user);
       return user;
    }

    @GetMapping("/addNotes")
    public String addNotes() {
        return "add_notes";
    }

    @GetMapping("/viewNotes")
    public String viewNotes(Model m, Principal p, @RequestParam(defaultValue = "0") Integer pageNo) {
        User user = getUser(p,m);
        Page<Notes> notes = notesService.getNotesByUser(user,pageNo);
        m.addAttribute("currentPage",pageNo);
        m.addAttribute("totalElements", notes.getTotalElements());
        m.addAttribute("totalPages",notes.getTotalPages());
        m.addAttribute("notesList",notes.getContent());
        return "view_notes";
    }



    @GetMapping("/editNotes/{id}")
    public String editNotes(@PathVariable int id, Model m) {
        Notes notes = notesService.getNotesById(id);
        m.addAttribute("n",notes);
        return "edit_notes";
    }

//    @PostMapping("/saveNotes")
//    public String saveNotes(@ModelAttribute Notes notes, HttpSession session, Principal p, Model m){
//        notes.setDate(LocalDate.now());
//
//
//        Notes saveNotes = notesService.saveNotes(notes);
//        if(saveNotes != null){
//            session.setAttribute("msg","Notes save Success");
//        } else{
//            session.setAttribute("msg","Something wrong");
//        }
//        return "redirect:/user/addNotes";
//
//    }

    @PostMapping("/saveNotes")
    public String saveNotes(@ModelAttribute Notes notes,
                            HttpSession session,
                            Principal principal) {
        try {
            // Get logged-in user
            User user = userRepo.findByEmail(principal.getName());

            // Set user and date
            notes.setUser(user);
            notes.setDate(LocalDate.now());

            // Save notes
            Notes savedNotes = notesService.saveNotes(notes);

            if (savedNotes != null) {
                session.setAttribute("msg", "Notes saved successfully");
            } else {
                session.setAttribute("msg", "Failed to save notes");
            }
        } catch (Exception e) {
            session.setAttribute("msg", "Error: " + e.getMessage());
        }

        return "redirect:/user/addNotes";
    }

    @PostMapping("/updateNotes")
    public String updateNotes(@ModelAttribute Notes notes, HttpSession session, Principal principal) {
        try {
            // Get logged-in user
            User user = userRepo.findByEmail(principal.getName());

            // Set user and date
            notes.setUser(user);
            notes.setDate(LocalDate.now());

            // Save notes
            Notes savedNotes = notesService.saveNotes(notes);

            if (savedNotes != null) {
                session.setAttribute("msg", "Notes update successfully");
            } else {
                session.setAttribute("msg", "Failed to update notes");
            }
        } catch (Exception e) {
            session.setAttribute("msg", "Error: " + e.getMessage());
        }

        return "redirect:/user/viewNotes";
    }

    @GetMapping("/deleteNotes/{id}")
    public String deleteNotes(@PathVariable int id,HttpSession session) {
        boolean f = notesService.deleteNotes(id);
        if(f){
            session.setAttribute("msg","Delete successfull");
        }else{
            session.setAttribute("msg","Something wrong");
        }
        return "redirect:/user/viewNotes";
    }





}


