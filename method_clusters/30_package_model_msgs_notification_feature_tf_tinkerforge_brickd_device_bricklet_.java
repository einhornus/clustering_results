public DimmableConfiguration createDimmableConfiguration()
  {
    DimmableConfigurationImpl dimmableConfiguration = new DimmableConfigurationImpl();
    return dimmableConfiguration;
  }
--------------------

public static IndustrialQuadRelayIDs getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      IndustrialQuadRelayIDs result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }
--------------------

public VCDeviceVoltage createVCDeviceVoltage()
  {
    VCDeviceVoltageImpl vcDeviceVoltage = new VCDeviceVoltageImpl();
    return vcDeviceVoltage;
  }
--------------------

