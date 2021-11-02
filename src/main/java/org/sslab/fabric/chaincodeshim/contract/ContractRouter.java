/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sslab.fabric.chaincodeshim.contract;

import org.sslab.fabric.chaincodeshim.Logging;
import org.sslab.fabric.chaincodeshim.contract.annotation.Serializer;
import org.sslab.fabric.chaincodeshim.contract.execution.ExecutionFactory;
import org.sslab.fabric.chaincodeshim.contract.execution.ExecutionService;
import org.sslab.fabric.chaincodeshim.contract.execution.InvocationRequest;
import org.sslab.fabric.chaincodeshim.contract.execution.SerializerInterface;
import org.sslab.fabric.chaincodeshim.contract.routing.ContractDefinition;
import org.sslab.fabric.chaincodeshim.contract.routing.RoutingRegistry;
import org.sslab.fabric.chaincodeshim.contract.routing.TxFunction;
import org.sslab.fabric.chaincodeshim.contract.routing.TypeRegistry;
import org.sslab.fabric.chaincodeshim.contract.routing.impl.RoutingRegistryImpl;
import org.sslab.fabric.chaincodeshim.contract.routing.impl.SerializerRegistryImpl;
import org.sslab.fabric.chaincodeshim.metrics.Metrics;
import org.sslab.fabric.chaincodeshim.shim.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Router class routes Init/Invoke requests to contracts. Implements
 * {@link Chaincode} interface.
 */
public class ContractRouter extends ChaincodeBase {
    private static Logger logger = Logger.getLogger(ContractRouter.class.getName());

    private static RoutingRegistry registry;
    private static TypeRegistry typeRegistry;

    // Store instances of SerializerInterfaces - identified by the contract
    // annotation (default is JSON)
    private SerializerRegistryImpl serializers;

    /**
     * Take the arguments from the cli, and initiate processing of cli options and
     * environment variables.
     *
     * Create the Contract scanner, and the Execution service
     *
     * @param args
     */
    public ContractRouter(final String[] args) {
        super.initializeLogging();
        super.processEnvironmentOptions();
        super.processCommandLineOptions(args);

//        final Properties props = super.getChaincodeConfig();
//        Metrics.initialize(props);

//        super.validateOptions();
        logger.fine("ContractRouter<init>");
        registry = new RoutingRegistryImpl();
        typeRegistry = TypeRegistry.getRegistry();

        serializers = new SerializerRegistryImpl();

        try {
            logger.info("contractrouter에서 try진입:");
            serializers.findAndSetContents();
        } catch (InstantiationException | IllegalAccessException e) {
            final ContractRuntimeException cre = new ContractRuntimeException("Unable to locate Serializers", e);
            logger.severe(() -> Logging.formatError(cre));
            throw new RuntimeException(cre);
        }

    }

    /**
     * Locate all the contracts that are available on the classpath.
     */
    public void findAllContracts() {
        registry.findAndSetContracts(this.typeRegistry);
    }

    /**
     * Start the chaincode container off and running.
     *
     * This will send the initial flow back to the peer
     *
     * @throws Exception
     */
    void startRouting() {
        try {
            super.connectToPeer();
        } catch (final Exception e) {
            final ContractRuntimeException cre = new ContractRuntimeException("Unable to start routing");
            logger.severe(() -> Logging.formatError(cre));
            throw cre;
        }
    }
    private Response processRequest(final ChaincodeStub stub) {
        logger.info(() -> "Got invoke routing request");
        try {
            if (stub.getStringArgs().size() > 0) {
                logger.info(() -> "Got the invoke request for:" + stub.getFunction() + " " + stub.getParameters());
                final InvocationRequest request = ExecutionFactory.getInstance().createRequest(stub);
                final TxFunction txFn = getRouting(request);

                // based on the routing information the serializer can be found
                // TRANSACTION target as this on the 'inbound' to invoke a tx
                final SerializerInterface si = serializers.getSerializer(txFn.getRouting().getSerializerName(), Serializer.TARGET.TRANSACTION);
                final ExecutionService executor = ExecutionFactory.getInstance().createExecutionService(si);

                logger.info(() -> "Got routing:" + txFn.getRouting());
                return executor.executeRequest(txFn, request, stub);
            } else {
                return ResponseUtils.newSuccessResponse();
            }
        } catch (final Throwable throwable) {
            return ResponseUtils.newErrorResponse(throwable);
        }
    }


    //modifying
//    private Response processRequest(final ChaincodeStub stub) {
//        logger.info(() -> "Got invoke routing request");
//        try {
//            if (stub.getStringArgs().size() > 0) {
//                logger.info(() -> "Got the invoke request for:" + stub.getFunction() + " " + stub.getParameters());
//                final InvocationRequest request = ExecutionFactory.getInstance().createRequest(stub);
//                final TxFunction txFn = getRouting(request);
////                logger.info(() -> "Got the txFn.getRouting():" + txFn.getRouting());
//
//
//                // based on the routing information the serializer can be found
//                // TRANSACTION target as this on the 'inbound' to invoke a tx
////                final SerializerInterface si = serializers.getSerializer(txFn.getRouting().getSerializerName(), Serializer.TARGET.TRANSACTION);
////                final ExecutionService executor = ExecutionFactory.getInstance().createExecutionService(si);
//
////                logger.info(() -> "Got routing:" + txFn.getRouting());
//                return executeRequest(request, stub);
//            } else {
//                return ResponseUtils.newSuccessResponse();
//            }
//        } catch (final Throwable throwable) {
//            return ResponseUtils.newErrorResponse(throwable);
//        }
//    }

    public Chaincode.Response executeRequest(final InvocationRequest req, final ChaincodeStub stub) {
        logger.fine(() -> "Routing Request");
        Chaincode.Response response;

        logger.info("namespace is " + req.getNamespace());
        String calssPath = "org.sslab.fabric.chaincode." + req.getNamespace() + "." + req.getNamespace();
        logger.info("calssPath is " + calssPath);

        try {
            Class<?> testClass = Class.forName(calssPath);
            Object newObj = testClass.newInstance();
            Method method = testClass.getDeclaredMethod(req.getMethod());

            List<Object> args = Arrays.asList(req.getArgs().toArray());
            Context context = new Context(stub);
            final Object value = method.invoke(newObj, req.getArgs().toArray());
            logger.info("value:" + value );
            args.add(0, context); // force context into 1st position, other elements move up


            if (value == null) {
                response = ResponseUtils.newSuccessResponse();
            } else {
                response = ResponseUtils.newSuccessResponse();
            }

        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            final String message = String.format("Could not execute contract method: ");
            throw new ContractRuntimeException(message, e);
        } catch (final InvocationTargetException | ClassNotFoundException e) {
            final Throwable cause = e.getCause();

            if (cause instanceof ChaincodeException) {
                throw (ChaincodeException) cause;
            } else {
                throw new ContractRuntimeException("Error during contract method execution", cause);
            }
        }

        return response;
    }

    @Override
    public Response invoke(final ChaincodeStub stub) {
        return  processRequest(stub);
    }

    @Override
    public Response init(final ChaincodeStub stub) {
        return processRequest(stub);
    }


    /**
     * Given the Invocation Request, return the routing object for this call.
     *
     * @param request
     * @return TxFunction for the request
     */
    TxFunction getRouting(final InvocationRequest request) {
        // request name is the fully qualified 'name:txname'
        if (registry.containsRoute(request)) {
            return registry.getTxFn(request);
        } else {
            logger.info(() -> "Namespace is " + request.getNamespace());
            final ContractDefinition contract = registry.getContract(request.getNamespace());
            return contract.getUnknownRoute();
        }
    }
//    /**
//     * Given the Invocation Request, return the routing object for this call.
//     *
//     * @param request
//     * @return TxFunction for the request
//     */
//    TxFunction getRouting(final InvocationRequest request) {
//        // request name is the fully qualified 'name:txname'
//        logger.info(() -> "Namespace is " + request);
//        return registry.getTxFn(request);
//
//    }

    /**
     * Main method to start the contract based chaincode.
     *
     * @param args
     */
    public static void main(final String[] args) {

        final ContractRouter cfc = new ContractRouter(args);
        System.out.println("여기까지는 도달했다!");
//        cfc.findAllContracts();

//        logger.fine(cfc.getRoutingRegistry().toString());

        // Create the Metadata ahead of time rather than have to produce every
        // time
//        MetadataBuilder.initialize(cfc.getRoutingRegistry(), cfc.getTypeRegistry());
//        logger.info(() -> "Metadata follows:" + MetadataBuilder.debugString());

        // commence routing, once this has returned the chaincode and contract api is
        // 'open for chaining'
//        cfc.startRouting();

    }

    public TypeRegistry getTypeRegistry() {
        return this.typeRegistry;
    }

    public RoutingRegistry getRoutingRegistry() {
        return this.registry;
    }
}
