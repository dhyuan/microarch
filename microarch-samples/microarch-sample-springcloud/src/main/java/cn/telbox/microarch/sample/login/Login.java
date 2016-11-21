package cn.telbox.microarch.sample.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by George on 2016/7/14.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    private Long id;
    private String loginId;
    private String password;

}
