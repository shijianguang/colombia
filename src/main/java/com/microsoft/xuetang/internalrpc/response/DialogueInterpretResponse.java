package com.microsoft.xuetang.internalrpc.response;

import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 7/29/2016.
 */
public class DialogueInterpretResponse {
    private String query;
    private List<Interpretation> interpretations;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Interpretation> getInterpretations() {
        return interpretations;
    }

    public void setInterpretations(List<Interpretation> interpretations) {
        this.interpretations = interpretations;
    }

    public static class Interpretation {
        private double logprob;
        private String parse;
        private List<Rule> rules;

        public double getLogprob() {
            return logprob;
        }

        public void setLogprob(double logprob) {
            this.logprob = logprob;
        }

        public String getParse() {
            return parse;
        }

        public void setParse(String parse) {
            this.parse = parse;
        }

        public List<Rule> getRules() {
            return rules;
        }

        public void setRules(List<Rule> rules) {
            this.rules = rules;
        }

        public static class Rule {
            private String name;
            private Map<String, String> output;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Map<String, String> getOutput() {
                return output;
            }

            public void setOutput(Map<String, String> output) {
                this.output = output;
            }
        }
    }
}
