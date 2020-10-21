package cn.yesomething.improjectclient.PageContact;

import java.util.Comparator;

public class ContactComparator  implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {//用于按字母排序
        int c1 = (o1.charAt(0) + "").toUpperCase().hashCode();
        int c2 = (o2.charAt(0) + "").toUpperCase().hashCode();

        boolean c1Flag = (c1 < "A".hashCode() || c1 > "Z".hashCode()); // 不是字母
        boolean c2Flag = (c2 < "A".hashCode() || c2 > "Z".hashCode()); // 不是字母
        if (c1Flag && !c2Flag) {
            return 1;
        } else if (!c1Flag && c2Flag) {
            return -1;
        }

        return c1 - c2;
    }

}
