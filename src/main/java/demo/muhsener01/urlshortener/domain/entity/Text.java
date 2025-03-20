package demo.muhsener01.urlshortener.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "texts")
@NoArgsConstructor
@Getter
@Setter
public class Text extends BaseEntity<String> {

    private String text;


    public Text(String code, String text) {
        this.id = code;
        this.text = text;
    }


}
