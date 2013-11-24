package com.liveperson.schema;

/**
 * Created with IntelliJ IDEA.
 * User: rans
 * Date: 9/15/13
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class RepoException extends Exception {
    private static final long serialVersionUID = 1L;
    public RepoException(Throwable cause) {
        super(cause);
    }
    public RepoException(String msg) {
        super(msg);
    }
}
