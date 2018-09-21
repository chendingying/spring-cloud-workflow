//package com.spring.cloud.flow.cmd;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import org.apache.commons.io.IOUtils;
//import org.flowable.bpmn.converter.BpmnXMLConverter;
//import org.flowable.bpmn.model.BpmnModel;
//import org.flowable.editor.language.json.converter.BpmnJsonConverter;
//import org.flowable.engine.ProcessEngineConfiguration;
//import org.flowable.engine.RepositoryService;
//import org.flowable.engine.common.api.FlowableException;
//import org.flowable.engine.common.impl.interceptor.Command;
//import org.flowable.engine.common.impl.interceptor.CommandContext;
//import org.flowable.engine.impl.util.CommandContextUtil;
//import org.flowable.image.ProcessDiagramGenerator;
//
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLStreamReader;
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Serializable;
//import java.util.Collections;
//
///**
// * Created by CDZ on 2018/9/15.
// */
//public class SaveFormCmd implements Command<Void>, Serializable {
//    private static final long serialVersionUID = 1L;
//    private String editorJson;
//    private String name;
//
//    public String getEditorJson() {
//        return editorJson;
//    }
//
//    public void setEditorJson(String editorJson) {
//        this.editorJson = editorJson;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//    public Void execute(CommandContext commandContext) {
//        ProcessEngineConfiguration processEngineConfiguration = CommandContextUtil.getProcessEngineConfiguration(commandContext);
//        RepositoryService repositoryService = processEngineConfiguration.getRepositoryService();
//
//        try {
//            byte[] bytes = editorJson.getBytes("utf-8");
//            repositoryService.addModelEditorSource();
//            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(bytes);
//            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
//            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
//            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
//            XMLInputFactory xif = XMLInputFactory.newInstance();
//            InputStreamReader xmlIn = new InputStreamReader(new ByteArrayInputStream(bpmnBytes), "UTF-8");
//            XMLStreamReader xtr = xif.createXMLStreamReader(xmlIn);
//            bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
//
//            ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
//            InputStream resource = diagramGenerator.generateDiagram(bpmnModel,"png",
//                    Collections.<String> emptyList(), Collections.<String> emptyList(),
//                    processEngineConfiguration.getActivityFontName(),
//                    processEngineConfiguration.getLabelFontName(),
//                    processEngineConfiguration.getAnnotationFontName(),
//                    processEngineConfiguration.getClassLoader(), 1.0);
//
//            repositoryService.addModelEditorSourceExtra(modelId, IOUtils.toByteArray(resource));
//        } catch (Exception e) {
//            throw new FlowableException("create model exception :"+e.getMessage());
//        }
//        return null;
//    }
//}
