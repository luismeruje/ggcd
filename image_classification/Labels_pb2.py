# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: Labels.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='Labels.proto',
  package='',
  syntax='proto2',
  serialized_options=_b('\n\010protobufB\013LabelsProto'),
  serialized_pb=_b('\n\x0cLabels.proto\"\x18\n\x06Labels\x12\x0e\n\x06labels\x18\x01 \x03(\tB\x17\n\x08protobufB\x0bLabelsProto')
)




_LABELS = _descriptor.Descriptor(
  name='Labels',
  full_name='Labels',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='labels', full_name='Labels.labels', index=0,
      number=1, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=16,
  serialized_end=40,
)

DESCRIPTOR.message_types_by_name['Labels'] = _LABELS
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

Labels = _reflection.GeneratedProtocolMessageType('Labels', (_message.Message,), dict(
  DESCRIPTOR = _LABELS,
  __module__ = 'Labels_pb2'
  # @@protoc_insertion_point(class_scope:Labels)
  ))
_sym_db.RegisterMessage(Labels)


DESCRIPTOR._options = None
# @@protoc_insertion_point(module_scope)
