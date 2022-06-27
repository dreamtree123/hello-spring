package hello.hellospring.domain;

public class Member {

    private Long id; //고객이 입력하는 id가 아닌, 데이터를 구분하기 위해서 시스템이 저장하는 id (일련번호같은 존재)
    private String name; //고객이 입력하는 name

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
