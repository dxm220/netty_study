/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package pers.dxm.thrift.source;


public enum Gender implements org.apache.thrift.TEnum {
  MALE(1),
  FEMALE(2);

  private final int value;

  private Gender(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static Gender findByValue(int value) { 
    switch (value) {
      case 1:
        return MALE;
      case 2:
        return FEMALE;
      default:
        return null;
    }
  }
}
