package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //꼭 써줘야 함!
public class HelloController  {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!"); //key:data, value:hello!!
        return "hello"; //resource폴더 > templates폴더 > hello.html
        // hello.html 화면을 렌더링하라는 의미, thymeleaf 템플릿엔진이 처리
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody //http는 header부와 body부로 나뉘어져 있는데, 그 body부에 return할 데이터를 직접 넣어주겠다는 의미.
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name; //템플릿엔진과의 차이점 : view를 거치지 않고 문자 그대로가 클라이언트에게 표시
    }

    @GetMapping("hello-api")
    @ResponseBody //반환값 default : json (xml 아님!)
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello(); //객체 생성
        hello.setName(name);
        return hello; //이전 방식들과 다르게, 문자가 아닌 객체를 넘김
    }

    static class Hello { //'static class'로 만들면, 'HelloController' 클래스 내에서 해당 클래스(Hello)를 또 사용할 수 있다. (ex: HelloController.Hello)
       private String name; //private으로 선언되었기 때문에 외부에서 바로 접근하지 못하므로 라이브러리 또는 getter/setter메소드를 통해 접근할 수 있다.

        //property 접근 방식 (getter/setter)
        public String getName() { //꺼낼때 -> getName
            return name;
        }

        public void setName(String name) { //넣을때 -> setName
            this.name = name;
        }
    }


}
