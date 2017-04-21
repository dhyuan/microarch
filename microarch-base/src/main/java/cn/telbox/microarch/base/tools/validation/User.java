package cn.telbox.microarch.base.tools.validation;

import cn.telbox.microarch.base.tools.validation.annotation.StringValueOptions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by dahui on 20/04/2017.
 */

@Getter
@Setter
@NoArgsConstructor
@Validated
public class User {

    @Size(min = 6, max = 30)
    private String loginId;

    @NotNull
    private String name;

    @Min(1) @Max(100)
    private Integer age;

    @Valid
    private List<Address> addresses;

    @StringValueOptions(options = "dog, cat")
    private String nickName;


}
