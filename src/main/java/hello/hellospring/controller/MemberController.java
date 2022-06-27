package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {

    //생성자 주입 방법
    private final MemberService memberService;

    @Autowired //'생성자 memberService'를 '스프링 컨테이너에 등록되어 있는 memberService'에 연결
    public MemberController(MemberService memberService) {

        this.memberService = memberService;
    }

    //필드 주입 방법
//    @Autowired private MemberService memberService; //final 빼야함!

    //setter 주입 방법
//    private MemberService memberService; //final 빼야함!
//
//    @Autowired
//    public void setMemberService(MemberService memberService) {
//        this.memberService = memberService;
//    }

    //회원 가입 (등록) 
    @GetMapping("/members/new") //get방식 : url에 직접 주소를 입력하는 방식
    public String createForm() {
        return "members/createMemberForm"; //(templates폴더의) members폴더의 createMemberForm.html파일로 이동
    }

    //MemberForm컨트롤러 생성 후
    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        System.out.println("member = " + member.getName());

        memberService.join(member);

        return "redirect:/"; //모든 작업이 끝났으면 홈화면으로 돌리기
    }

    @GetMapping("/members") //get방식 : url에 직접 주소를 입력하는 방식
    public String list(Model model) {
        List<Member> members = memberService.findMembers(); //모든 회원들을 List로 조회
        model.addAttribute("members",members); //모든 회원 List 자체를 model에 담아서 뷰템플릿에 넘김
        return "members/memberList";
    }
}
