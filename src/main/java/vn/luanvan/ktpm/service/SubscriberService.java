package vn.luanvan.ktpm.service;

import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Job;
import vn.luanvan.ktpm.domain.Skill;
import vn.luanvan.ktpm.domain.Subscriber;
import vn.luanvan.ktpm.domain.response.email.ResEmailJob;
import vn.luanvan.ktpm.repository.JobRepository;
import vn.luanvan.ktpm.repository.SkillRepository;
import vn.luanvan.ktpm.repository.SubcriberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriberService {
    private final SubcriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubcriberRepository subscriberRepository, SkillRepository skillRepository, JobRepository jobRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public boolean isExistByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber handleCreateSubscriber(Subscriber subscriber) {
        // check skills
        if (subscriber.getSkills() != null) {
            List<Long> reqSkills = subscriber.getSkills().stream().map(
                    item -> item.getId()
            ).toList();

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subscriber.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subscriber);
    }

    public Subscriber handleGetSubcriberById(long id) {
        Optional<Subscriber> subscriberOptional = this.subscriberRepository.findById(id);
        return subscriberOptional.orElse(null);
    }

    public Subscriber handleUpdateSubscriber(Subscriber subscriberDB, Subscriber subscriberReq) {
        // check skills
        if (subscriberReq.getSkills() != null) {
            List<Long> reqSkills = subscriberReq.getSkills().stream().map(
                    item -> item.getId()
            ).toList();

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subscriberDB.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subscriberDB);
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(
                skill -> new ResEmailJob.SkillEmail(skill.getName())
        ).collect(Collectors.toList());
        res.setSkills(s);
        return res;
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {

                         List<ResEmailJob> arr = listJobs.stream().map(
                         job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

}
