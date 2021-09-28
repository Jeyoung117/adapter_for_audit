// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: sharedlog.proto

package org.hyperledger.fabric.protos.peer;

public interface ReqAppendOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.sslab.adapter.ReqAppend)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string ChannelID = 1;</code>
   */
  java.lang.String getChannelID();
  /**
   * <code>string ChannelID = 1;</code>
   */
  com.google.protobuf.ByteString
      getChannelIDBytes();

  /**
   * <code>string chaincodeID = 2;</code>
   */
  java.lang.String getChaincodeID();
  /**
   * <code>string chaincodeID = 2;</code>
   */
  com.google.protobuf.ByteString
      getChaincodeIDBytes();

  /**
   * <pre>
   *    string objectname = 3;
   * </pre>
   *
   * <code>bytes txContext = 3;</code>
   */
  com.google.protobuf.ByteString getTxContext();

  /**
   * <code>bytes payload = 4;</code>
   */
  com.google.protobuf.ByteString getPayload();

  /**
   * <code>bytes writeset = 5;</code>
   */
  com.google.protobuf.ByteString getWriteset();

  /**
   * <code>bytes readset = 6;</code>
   */
  com.google.protobuf.ByteString getReadset();
}
