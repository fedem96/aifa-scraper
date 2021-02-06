package com.fedem96.requester;

import java.io.IOException;
import java.util.Map;

public abstract class Requester {

    public abstract String sendRequest(Map<String, String> parameters) throws IOException;

}
