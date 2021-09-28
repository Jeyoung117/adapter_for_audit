// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: sharedlog.proto

package org.hyperledger.fabric.protos.peer;

/**
 * Protobuf type {@code org.sslab.adapter.SuccessResponse}
 */
public  final class SuccessResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:org.sslab.adapter.SuccessResponse)
    SuccessResponseOrBuilder {
private static final long serialVersionUID = 0L;
  // Use SuccessResponse.newBuilder() to construct.
  private SuccessResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private SuccessResponse() {
    successMessage_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private SuccessResponse(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            successMessage_ = s;
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.hyperledger.fabric.protos.peer.adapterservice.internal_static_org_sslab_adapter_SuccessResponse_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.hyperledger.fabric.protos.peer.adapterservice.internal_static_org_sslab_adapter_SuccessResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.hyperledger.fabric.protos.peer.SuccessResponse.class, org.hyperledger.fabric.protos.peer.SuccessResponse.Builder.class);
  }

  public static final int SUCCESSMESSAGE_FIELD_NUMBER = 1;
  private volatile java.lang.Object successMessage_;
  /**
   * <code>string SuccessMessage = 1;</code>
   */
  public java.lang.String getSuccessMessage() {
    java.lang.Object ref = successMessage_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      successMessage_ = s;
      return s;
    }
  }
  /**
   * <code>string SuccessMessage = 1;</code>
   */
  public com.google.protobuf.ByteString
      getSuccessMessageBytes() {
    java.lang.Object ref = successMessage_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      successMessage_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getSuccessMessageBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, successMessage_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getSuccessMessageBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, successMessage_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof org.hyperledger.fabric.protos.peer.SuccessResponse)) {
      return super.equals(obj);
    }
    org.hyperledger.fabric.protos.peer.SuccessResponse other = (org.hyperledger.fabric.protos.peer.SuccessResponse) obj;

    boolean result = true;
    result = result && getSuccessMessage()
        .equals(other.getSuccessMessage());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + SUCCESSMESSAGE_FIELD_NUMBER;
    hash = (53 * hash) + getSuccessMessage().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.hyperledger.fabric.protos.peer.SuccessResponse parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.hyperledger.fabric.protos.peer.SuccessResponse parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.hyperledger.fabric.protos.peer.SuccessResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.hyperledger.fabric.protos.peer.SuccessResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.hyperledger.fabric.protos.peer.SuccessResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.hyperledger.fabric.protos.peer.SuccessResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.hyperledger.fabric.protos.peer.SuccessResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.hyperledger.fabric.protos.peer.SuccessResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.hyperledger.fabric.protos.peer.SuccessResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.hyperledger.fabric.protos.peer.SuccessResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.hyperledger.fabric.protos.peer.SuccessResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.hyperledger.fabric.protos.peer.SuccessResponse parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(org.hyperledger.fabric.protos.peer.SuccessResponse prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code org.sslab.adapter.SuccessResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:org.sslab.adapter.SuccessResponse)
      org.hyperledger.fabric.protos.peer.SuccessResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.hyperledger.fabric.protos.peer.adapterservice.internal_static_org_sslab_adapter_SuccessResponse_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.hyperledger.fabric.protos.peer.adapterservice.internal_static_org_sslab_adapter_SuccessResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.hyperledger.fabric.protos.peer.SuccessResponse.class, org.hyperledger.fabric.protos.peer.SuccessResponse.Builder.class);
    }

    // Construct using org.hyperledger.fabric.protos.peer.SuccessResponse.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      successMessage_ = "";

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.hyperledger.fabric.protos.peer.adapterservice.internal_static_org_sslab_adapter_SuccessResponse_descriptor;
    }

    public org.hyperledger.fabric.protos.peer.SuccessResponse getDefaultInstanceForType() {
      return org.hyperledger.fabric.protos.peer.SuccessResponse.getDefaultInstance();
    }

    public org.hyperledger.fabric.protos.peer.SuccessResponse build() {
      org.hyperledger.fabric.protos.peer.SuccessResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public org.hyperledger.fabric.protos.peer.SuccessResponse buildPartial() {
      org.hyperledger.fabric.protos.peer.SuccessResponse result = new org.hyperledger.fabric.protos.peer.SuccessResponse(this);
      result.successMessage_ = successMessage_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.hyperledger.fabric.protos.peer.SuccessResponse) {
        return mergeFrom((org.hyperledger.fabric.protos.peer.SuccessResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.hyperledger.fabric.protos.peer.SuccessResponse other) {
      if (other == org.hyperledger.fabric.protos.peer.SuccessResponse.getDefaultInstance()) return this;
      if (!other.getSuccessMessage().isEmpty()) {
        successMessage_ = other.successMessage_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      org.hyperledger.fabric.protos.peer.SuccessResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.hyperledger.fabric.protos.peer.SuccessResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object successMessage_ = "";
    /**
     * <code>string SuccessMessage = 1;</code>
     */
    public java.lang.String getSuccessMessage() {
      java.lang.Object ref = successMessage_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        successMessage_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string SuccessMessage = 1;</code>
     */
    public com.google.protobuf.ByteString
        getSuccessMessageBytes() {
      java.lang.Object ref = successMessage_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        successMessage_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string SuccessMessage = 1;</code>
     */
    public Builder setSuccessMessage(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      successMessage_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string SuccessMessage = 1;</code>
     */
    public Builder clearSuccessMessage() {
      
      successMessage_ = getDefaultInstance().getSuccessMessage();
      onChanged();
      return this;
    }
    /**
     * <code>string SuccessMessage = 1;</code>
     */
    public Builder setSuccessMessageBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      successMessage_ = value;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:org.sslab.adapter.SuccessResponse)
  }

  // @@protoc_insertion_point(class_scope:org.sslab.adapter.SuccessResponse)
  private static final org.hyperledger.fabric.protos.peer.SuccessResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.hyperledger.fabric.protos.peer.SuccessResponse();
  }

  public static org.hyperledger.fabric.protos.peer.SuccessResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<SuccessResponse>
      PARSER = new com.google.protobuf.AbstractParser<SuccessResponse>() {
    public SuccessResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new SuccessResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<SuccessResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<SuccessResponse> getParserForType() {
    return PARSER;
  }

  public org.hyperledger.fabric.protos.peer.SuccessResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

