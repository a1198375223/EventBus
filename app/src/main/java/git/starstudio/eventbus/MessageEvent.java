package git.starstudio.eventbus;

/**
 * Created by Loser on 2017/3/26.
 * 首先先创建一个MessageEvent类来定义事件
 */

public class MessageEvent {
    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }
}
