package web.compare.util;

import org.junit.Test;

public class sad {
    @Test
    public void name() {
        String a = "outerHtml<a class=\"btn btn-xl btn-outline-secundary js-scroll-trigger _ngcontent-xdt-2\" href=\"\" style=\"margin-top: 35px; margin-right:10px; width:200px;\">Login</a> innerHtml: Login";
        String b = "outerHtml<a class=\"btn btn-xl btn-outline-secundary js-scroll-trigger _ngcontent-rgc-2\" href=\"\" style=\"margin-top: 35px; margin-right:10px; width:200px;\">Login</a> innerHtml: Login";
        System.out.println(a.equals(b));
    }
}
