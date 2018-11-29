package ustc.sse.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author LRK
 * @project_name UseSC
 * @package_name ustc.sse.domain
 * @date 2018/11/28 16:22
 * @description God Bless, No Bug!
 */
@Getter@Setter
public class User {
    private Integer userId;
    private String userName;
    private String userPass;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                '}';
    }
}
