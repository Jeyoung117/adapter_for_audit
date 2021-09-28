package org.hyperledger.fabric.protos.peer;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.14.0)",
    comments = "Source: sharedlog.proto")
public final class CorfuConnectGrpc {

  private CorfuConnectGrpc() {}

  public static final String SERVICE_NAME = "org.sslab.adapter.CorfuConnect";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqAppend,
      org.hyperledger.fabric.protos.peer.ResAppend> getSendTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendTransaction",
      requestType = org.hyperledger.fabric.protos.peer.ReqAppend.class,
      responseType = org.hyperledger.fabric.protos.peer.ResAppend.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqAppend,
      org.hyperledger.fabric.protos.peer.ResAppend> getSendTransactionMethod() {
    io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqAppend, org.hyperledger.fabric.protos.peer.ResAppend> getSendTransactionMethod;
    if ((getSendTransactionMethod = CorfuConnectGrpc.getSendTransactionMethod) == null) {
      synchronized (CorfuConnectGrpc.class) {
        if ((getSendTransactionMethod = CorfuConnectGrpc.getSendTransactionMethod) == null) {
          CorfuConnectGrpc.getSendTransactionMethod = getSendTransactionMethod = 
              io.grpc.MethodDescriptor.<org.hyperledger.fabric.protos.peer.ReqAppend, org.hyperledger.fabric.protos.peer.ResAppend>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "org.sslab.adapter.CorfuConnect", "SendTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ReqAppend.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ResAppend.getDefaultInstance()))
                  .setSchemaDescriptor(new CorfuConnectMethodDescriptorSupplier("SendTransaction"))
                  .build();
          }
        }
     }
     return getSendTransactionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqTxcont,
      org.hyperledger.fabric.protos.peer.SuccessResponse> getIssueSnapshotTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "IssueSnapshotToken",
      requestType = org.hyperledger.fabric.protos.peer.ReqTxcont.class,
      responseType = org.hyperledger.fabric.protos.peer.SuccessResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqTxcont,
      org.hyperledger.fabric.protos.peer.SuccessResponse> getIssueSnapshotTokenMethod() {
    io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqTxcont, org.hyperledger.fabric.protos.peer.SuccessResponse> getIssueSnapshotTokenMethod;
    if ((getIssueSnapshotTokenMethod = CorfuConnectGrpc.getIssueSnapshotTokenMethod) == null) {
      synchronized (CorfuConnectGrpc.class) {
        if ((getIssueSnapshotTokenMethod = CorfuConnectGrpc.getIssueSnapshotTokenMethod) == null) {
          CorfuConnectGrpc.getIssueSnapshotTokenMethod = getIssueSnapshotTokenMethod = 
              io.grpc.MethodDescriptor.<org.hyperledger.fabric.protos.peer.ReqTxcont, org.hyperledger.fabric.protos.peer.SuccessResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "org.sslab.adapter.CorfuConnect", "IssueSnapshotToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ReqTxcont.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.SuccessResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new CorfuConnectMethodDescriptorSupplier("IssueSnapshotToken"))
                  .build();
          }
        }
     }
     return getIssueSnapshotTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqRead,
      org.hyperledger.fabric.protos.peer.ResRead> getGetTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTransaction",
      requestType = org.hyperledger.fabric.protos.peer.ReqRead.class,
      responseType = org.hyperledger.fabric.protos.peer.ResRead.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqRead,
      org.hyperledger.fabric.protos.peer.ResRead> getGetTransactionMethod() {
    io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqRead, org.hyperledger.fabric.protos.peer.ResRead> getGetTransactionMethod;
    if ((getGetTransactionMethod = CorfuConnectGrpc.getGetTransactionMethod) == null) {
      synchronized (CorfuConnectGrpc.class) {
        if ((getGetTransactionMethod = CorfuConnectGrpc.getGetTransactionMethod) == null) {
          CorfuConnectGrpc.getGetTransactionMethod = getGetTransactionMethod = 
              io.grpc.MethodDescriptor.<org.hyperledger.fabric.protos.peer.ReqRead, org.hyperledger.fabric.protos.peer.ResRead>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "org.sslab.adapter.CorfuConnect", "GetTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ReqRead.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ResRead.getDefaultInstance()))
                  .setSchemaDescriptor(new CorfuConnectMethodDescriptorSupplier("GetTransaction"))
                  .build();
          }
        }
     }
     return getGetTransactionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqCheck,
      org.hyperledger.fabric.protos.peer.ResCheck> getCheckVersionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckVersion",
      requestType = org.hyperledger.fabric.protos.peer.ReqCheck.class,
      responseType = org.hyperledger.fabric.protos.peer.ResCheck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqCheck,
      org.hyperledger.fabric.protos.peer.ResCheck> getCheckVersionMethod() {
    io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqCheck, org.hyperledger.fabric.protos.peer.ResCheck> getCheckVersionMethod;
    if ((getCheckVersionMethod = CorfuConnectGrpc.getCheckVersionMethod) == null) {
      synchronized (CorfuConnectGrpc.class) {
        if ((getCheckVersionMethod = CorfuConnectGrpc.getCheckVersionMethod) == null) {
          CorfuConnectGrpc.getCheckVersionMethod = getCheckVersionMethod = 
              io.grpc.MethodDescriptor.<org.hyperledger.fabric.protos.peer.ReqCheck, org.hyperledger.fabric.protos.peer.ResCheck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "org.sslab.adapter.CorfuConnect", "CheckVersion"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ReqCheck.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ResCheck.getDefaultInstance()))
                  .setSchemaDescriptor(new CorfuConnectMethodDescriptorSupplier("CheckVersion"))
                  .build();
          }
        }
     }
     return getCheckVersionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqGet,
      org.hyperledger.fabric.protos.peer.ResGet> getGetStringStateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getStringState",
      requestType = org.hyperledger.fabric.protos.peer.ReqGet.class,
      responseType = org.hyperledger.fabric.protos.peer.ResGet.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqGet,
      org.hyperledger.fabric.protos.peer.ResGet> getGetStringStateMethod() {
    io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqGet, org.hyperledger.fabric.protos.peer.ResGet> getGetStringStateMethod;
    if ((getGetStringStateMethod = CorfuConnectGrpc.getGetStringStateMethod) == null) {
      synchronized (CorfuConnectGrpc.class) {
        if ((getGetStringStateMethod = CorfuConnectGrpc.getGetStringStateMethod) == null) {
          CorfuConnectGrpc.getGetStringStateMethod = getGetStringStateMethod = 
              io.grpc.MethodDescriptor.<org.hyperledger.fabric.protos.peer.ReqGet, org.hyperledger.fabric.protos.peer.ResGet>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "org.sslab.adapter.CorfuConnect", "getStringState"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ReqGet.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ResGet.getDefaultInstance()))
                  .setSchemaDescriptor(new CorfuConnectMethodDescriptorSupplier("getStringState"))
                  .build();
          }
        }
     }
     return getGetStringStateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqPut,
      org.hyperledger.fabric.protos.peer.ResPut> getPutStringStateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "putStringState",
      requestType = org.hyperledger.fabric.protos.peer.ReqPut.class,
      responseType = org.hyperledger.fabric.protos.peer.ResPut.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqPut,
      org.hyperledger.fabric.protos.peer.ResPut> getPutStringStateMethod() {
    io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ReqPut, org.hyperledger.fabric.protos.peer.ResPut> getPutStringStateMethod;
    if ((getPutStringStateMethod = CorfuConnectGrpc.getPutStringStateMethod) == null) {
      synchronized (CorfuConnectGrpc.class) {
        if ((getPutStringStateMethod = CorfuConnectGrpc.getPutStringStateMethod) == null) {
          CorfuConnectGrpc.getPutStringStateMethod = getPutStringStateMethod = 
              io.grpc.MethodDescriptor.<org.hyperledger.fabric.protos.peer.ReqPut, org.hyperledger.fabric.protos.peer.ResPut>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "org.sslab.adapter.CorfuConnect", "putStringState"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ReqPut.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ResPut.getDefaultInstance()))
                  .setSchemaDescriptor(new CorfuConnectMethodDescriptorSupplier("putStringState"))
                  .build();
          }
        }
     }
     return getPutStringStateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ProposalResponsePackage1.ProposalResponse,
      org.hyperledger.fabric.protos.peer.ResCommit> getCommitTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CommitTransaction",
      requestType = org.hyperledger.fabric.protos.peer.ProposalResponsePackage1.ProposalResponse.class,
      responseType = org.hyperledger.fabric.protos.peer.ResCommit.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ProposalResponsePackage1.ProposalResponse,
      org.hyperledger.fabric.protos.peer.ResCommit> getCommitTransactionMethod() {
    io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.ProposalResponsePackage1.ProposalResponse, org.hyperledger.fabric.protos.peer.ResCommit> getCommitTransactionMethod;
    if ((getCommitTransactionMethod = CorfuConnectGrpc.getCommitTransactionMethod) == null) {
      synchronized (CorfuConnectGrpc.class) {
        if ((getCommitTransactionMethod = CorfuConnectGrpc.getCommitTransactionMethod) == null) {
          CorfuConnectGrpc.getCommitTransactionMethod = getCommitTransactionMethod = 
              io.grpc.MethodDescriptor.<org.hyperledger.fabric.protos.peer.ProposalResponsePackage1.ProposalResponse, org.hyperledger.fabric.protos.peer.ResCommit>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "org.sslab.adapter.CorfuConnect", "CommitTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ProposalResponsePackage1.ProposalResponse.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.ResCommit.getDefaultInstance()))
                  .setSchemaDescriptor(new CorfuConnectMethodDescriptorSupplier("CommitTransaction"))
                  .build();
          }
        }
     }
     return getCommitTransactionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CorfuConnectStub newStub(io.grpc.Channel channel) {
    return new CorfuConnectStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CorfuConnectBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new CorfuConnectBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CorfuConnectFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new CorfuConnectFutureStub(channel);
  }

  /**
   */
  public static abstract class CorfuConnectImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendTransaction(org.hyperledger.fabric.protos.peer.ReqAppend request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResAppend> responseObserver) {
      asyncUnimplementedUnaryCall(getSendTransactionMethod(), responseObserver);
    }

    /**
     */
    public void issueSnapshotToken(org.hyperledger.fabric.protos.peer.ReqTxcont request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.SuccessResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getIssueSnapshotTokenMethod(), responseObserver);
    }

    /**
     */
    public void getTransaction(org.hyperledger.fabric.protos.peer.ReqRead request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResRead> responseObserver) {
      asyncUnimplementedUnaryCall(getGetTransactionMethod(), responseObserver);
    }

    /**
     */
    public void checkVersion(org.hyperledger.fabric.protos.peer.ReqCheck request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResCheck> responseObserver) {
      asyncUnimplementedUnaryCall(getCheckVersionMethod(), responseObserver);
    }

    /**
     */
    public void getStringState(org.hyperledger.fabric.protos.peer.ReqGet request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResGet> responseObserver) {
      asyncUnimplementedUnaryCall(getGetStringStateMethod(), responseObserver);
    }

    /**
     */
    public void putStringState(org.hyperledger.fabric.protos.peer.ReqPut request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResPut> responseObserver) {
      asyncUnimplementedUnaryCall(getPutStringStateMethod(), responseObserver);
    }

    /**
     * <pre>
     *    rpc CommitTransaction(ReqCommit) returns (ResCommit);
     * </pre>
     */
    public void commitTransaction(org.hyperledger.fabric.protos.peer.ProposalResponsePackage1.ProposalResponse request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResCommit> responseObserver) {
      asyncUnimplementedUnaryCall(getCommitTransactionMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendTransactionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.hyperledger.fabric.protos.peer.ReqAppend,
                org.hyperledger.fabric.protos.peer.ResAppend>(
                  this, METHODID_SEND_TRANSACTION)))
          .addMethod(
            getIssueSnapshotTokenMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.hyperledger.fabric.protos.peer.ReqTxcont,
                org.hyperledger.fabric.protos.peer.SuccessResponse>(
                  this, METHODID_ISSUE_SNAPSHOT_TOKEN)))
          .addMethod(
            getGetTransactionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.hyperledger.fabric.protos.peer.ReqRead,
                org.hyperledger.fabric.protos.peer.ResRead>(
                  this, METHODID_GET_TRANSACTION)))
          .addMethod(
            getCheckVersionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.hyperledger.fabric.protos.peer.ReqCheck,
                org.hyperledger.fabric.protos.peer.ResCheck>(
                  this, METHODID_CHECK_VERSION)))
          .addMethod(
            getGetStringStateMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.hyperledger.fabric.protos.peer.ReqGet,
                org.hyperledger.fabric.protos.peer.ResGet>(
                  this, METHODID_GET_STRING_STATE)))
          .addMethod(
            getPutStringStateMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.hyperledger.fabric.protos.peer.ReqPut,
                org.hyperledger.fabric.protos.peer.ResPut>(
                  this, METHODID_PUT_STRING_STATE)))
          .addMethod(
            getCommitTransactionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.hyperledger.fabric.protos.peer.ProposalResponsePackage1.ProposalResponse,
                org.hyperledger.fabric.protos.peer.ResCommit>(
                  this, METHODID_COMMIT_TRANSACTION)))
          .build();
    }
  }

  /**
   */
  public static final class CorfuConnectStub extends io.grpc.stub.AbstractStub<CorfuConnectStub> {
    private CorfuConnectStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CorfuConnectStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CorfuConnectStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CorfuConnectStub(channel, callOptions);
    }

    /**
     */
    public void sendTransaction(org.hyperledger.fabric.protos.peer.ReqAppend request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResAppend> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void issueSnapshotToken(org.hyperledger.fabric.protos.peer.ReqTxcont request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.SuccessResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getIssueSnapshotTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTransaction(org.hyperledger.fabric.protos.peer.ReqRead request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResRead> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetTransactionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void checkVersion(org.hyperledger.fabric.protos.peer.ReqCheck request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResCheck> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCheckVersionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getStringState(org.hyperledger.fabric.protos.peer.ReqGet request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResGet> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetStringStateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void putStringState(org.hyperledger.fabric.protos.peer.ReqPut request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResPut> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPutStringStateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *    rpc CommitTransaction(ReqCommit) returns (ResCommit);
     * </pre>
     */
    public void commitTransaction(org.hyperledger.fabric.protos.peer.ProposalResponsePackage1.ProposalResponse request,
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResCommit> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCommitTransactionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CorfuConnectBlockingStub extends io.grpc.stub.AbstractStub<CorfuConnectBlockingStub> {
    private CorfuConnectBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CorfuConnectBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CorfuConnectBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CorfuConnectBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.hyperledger.fabric.protos.peer.ResAppend sendTransaction(org.hyperledger.fabric.protos.peer.ReqAppend request) {
      return blockingUnaryCall(
          getChannel(), getSendTransactionMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.hyperledger.fabric.protos.peer.SuccessResponse issueSnapshotToken(org.hyperledger.fabric.protos.peer.ReqTxcont request) {
      return blockingUnaryCall(
          getChannel(), getIssueSnapshotTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.hyperledger.fabric.protos.peer.ResRead getTransaction(org.hyperledger.fabric.protos.peer.ReqRead request) {
      return blockingUnaryCall(
          getChannel(), getGetTransactionMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.hyperledger.fabric.protos.peer.ResCheck checkVersion(org.hyperledger.fabric.protos.peer.ReqCheck request) {
      return blockingUnaryCall(
          getChannel(), getCheckVersionMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.hyperledger.fabric.protos.peer.ResGet getStringState(org.hyperledger.fabric.protos.peer.ReqGet request) {
      return blockingUnaryCall(
          getChannel(), getGetStringStateMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.hyperledger.fabric.protos.peer.ResPut putStringState(org.hyperledger.fabric.protos.peer.ReqPut request) {
      return blockingUnaryCall(
          getChannel(), getPutStringStateMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *    rpc CommitTransaction(ReqCommit) returns (ResCommit);
     * </pre>
     */
    public org.hyperledger.fabric.protos.peer.ResCommit commitTransaction(org.hyperledger.fabric.protos.peer.ProposalResponsePackage1.ProposalResponse request) {
      return blockingUnaryCall(
          getChannel(), getCommitTransactionMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CorfuConnectFutureStub extends io.grpc.stub.AbstractStub<CorfuConnectFutureStub> {
    private CorfuConnectFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CorfuConnectFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CorfuConnectFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CorfuConnectFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.hyperledger.fabric.protos.peer.ResAppend> sendTransaction(
        org.hyperledger.fabric.protos.peer.ReqAppend request) {
      return futureUnaryCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.hyperledger.fabric.protos.peer.SuccessResponse> issueSnapshotToken(
        org.hyperledger.fabric.protos.peer.ReqTxcont request) {
      return futureUnaryCall(
          getChannel().newCall(getIssueSnapshotTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.hyperledger.fabric.protos.peer.ResRead> getTransaction(
        org.hyperledger.fabric.protos.peer.ReqRead request) {
      return futureUnaryCall(
          getChannel().newCall(getGetTransactionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.hyperledger.fabric.protos.peer.ResCheck> checkVersion(
        org.hyperledger.fabric.protos.peer.ReqCheck request) {
      return futureUnaryCall(
          getChannel().newCall(getCheckVersionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.hyperledger.fabric.protos.peer.ResGet> getStringState(
        org.hyperledger.fabric.protos.peer.ReqGet request) {
      return futureUnaryCall(
          getChannel().newCall(getGetStringStateMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.hyperledger.fabric.protos.peer.ResPut> putStringState(
        org.hyperledger.fabric.protos.peer.ReqPut request) {
      return futureUnaryCall(
          getChannel().newCall(getPutStringStateMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *    rpc CommitTransaction(ReqCommit) returns (ResCommit);
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.hyperledger.fabric.protos.peer.ResCommit> commitTransaction(
        org.hyperledger.fabric.protos.peer.ProposalResponsePackage1.ProposalResponse request) {
      return futureUnaryCall(
          getChannel().newCall(getCommitTransactionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_TRANSACTION = 0;
  private static final int METHODID_ISSUE_SNAPSHOT_TOKEN = 1;
  private static final int METHODID_GET_TRANSACTION = 2;
  private static final int METHODID_CHECK_VERSION = 3;
  private static final int METHODID_GET_STRING_STATE = 4;
  private static final int METHODID_PUT_STRING_STATE = 5;
  private static final int METHODID_COMMIT_TRANSACTION = 6;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CorfuConnectImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CorfuConnectImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_TRANSACTION:
          serviceImpl.sendTransaction((org.hyperledger.fabric.protos.peer.ReqAppend) request,
              (io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResAppend>) responseObserver);
          break;
        case METHODID_ISSUE_SNAPSHOT_TOKEN:
          serviceImpl.issueSnapshotToken((org.hyperledger.fabric.protos.peer.ReqTxcont) request,
              (io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.SuccessResponse>) responseObserver);
          break;
        case METHODID_GET_TRANSACTION:
          serviceImpl.getTransaction((org.hyperledger.fabric.protos.peer.ReqRead) request,
              (io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResRead>) responseObserver);
          break;
        case METHODID_CHECK_VERSION:
          serviceImpl.checkVersion((org.hyperledger.fabric.protos.peer.ReqCheck) request,
              (io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResCheck>) responseObserver);
          break;
        case METHODID_GET_STRING_STATE:
          serviceImpl.getStringState((org.hyperledger.fabric.protos.peer.ReqGet) request,
              (io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResGet>) responseObserver);
          break;
        case METHODID_PUT_STRING_STATE:
          serviceImpl.putStringState((org.hyperledger.fabric.protos.peer.ReqPut) request,
              (io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResPut>) responseObserver);
          break;
        case METHODID_COMMIT_TRANSACTION:
          serviceImpl.commitTransaction((org.hyperledger.fabric.protos.peer.ProposalResponsePackage1.ProposalResponse) request,
              (io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.ResCommit>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class CorfuConnectBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CorfuConnectBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.hyperledger.fabric.protos.peer.adapterservice.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CorfuConnect");
    }
  }

  private static final class CorfuConnectFileDescriptorSupplier
      extends CorfuConnectBaseDescriptorSupplier {
    CorfuConnectFileDescriptorSupplier() {}
  }

  private static final class CorfuConnectMethodDescriptorSupplier
      extends CorfuConnectBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CorfuConnectMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CorfuConnectGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CorfuConnectFileDescriptorSupplier())
              .addMethod(getSendTransactionMethod())
              .addMethod(getIssueSnapshotTokenMethod())
              .addMethod(getGetTransactionMethod())
              .addMethod(getCheckVersionMethod())
              .addMethod(getGetStringStateMethod())
              .addMethod(getPutStringStateMethod())
              .addMethod(getCommitTransactionMethod())
              .build();
        }
      }
    }
    return result;
  }
}
