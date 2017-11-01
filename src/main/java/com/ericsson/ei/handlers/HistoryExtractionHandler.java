/*
    Copyright 2017 Ericsson AB.
    For a full list of individual contributors, please see the commit history.
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.ericsson.ei.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ericsson.ei.jmespath.JmesPathInterface;
import com.ericsson.ei.jsonmerge.MergeHandler;
import com.ericsson.ei.jsonmerge.MergePrepare;
import com.ericsson.ei.rules.RulesObject;
import com.fasterxml.jackson.databind.JsonNode;

// TODO: Auto-generated Javadoc
/**
 * The Class HistoryExtractionHandler.
 */
public class HistoryExtractionHandler {

    static Logger log = (Logger) LoggerFactory.getLogger(HistoryExtractionHandler.class);

    @Autowired private JmesPathInterface jmesPathInterface;
    @Autowired private MergeHandler mergeHandler;
    @Autowired private MergePrepare mergePrepare;



    /**
     * Run history extraction.
     *
     * @param aggregatedObjectId the aggregated object id
     * @param rulesObject the rules object
     * @param event the event
     * @param pathInAggregatedObject the path in aggregated object
     * @return the string
     */
    public String runHistoryExtraction(String aggregatedObjectId, RulesObject rulesObject, String event, String pathInAggregatedObject) {
        JsonNode extractedContent;
        extractedContent = extractContent(rulesObject, event);
        mergeHandler.mergeObject(aggregatedObjectId, rulesObject, event, extractedContent, pathInAggregatedObject);

        String updatedPathInAggregatedObject = pathInAggregatedObject + getPathFromExtractedContent(extractedContent);
        return updatedPathInAggregatedObject;
    }

    /**
     * Gets the path from extracted content.
     *
     * @param extractedContent the extracted content
     * @return the path from extracted content
     */
    private String getPathFromExtractedContent(JsonNode extractedContent) {
        String mergePath = mergePrepare.getMergePath(extractedContent.toString(), null);
        return mergePath;
    }

    /**
     * Extract content.
     *
     * @param rulesObject the rules object
     * @param event the event
     * @return the json node
     */
    private JsonNode extractContent(RulesObject rulesObject, String event) {
        String extractonRules;
        extractonRules = rulesObject.getHistoryExtractionRules();
        return jmesPathInterface.runRuleOnEvent(extractonRules, event);
    }


}



