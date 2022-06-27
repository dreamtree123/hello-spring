package hello.hellospring.controller;

public class MemberForm  {

    private String name; //createMemberForm.html파일의 <input>태그의 name값과 매칭

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
