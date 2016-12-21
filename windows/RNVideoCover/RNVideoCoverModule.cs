using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Com.Reactlibrary.RNVideoCover
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNVideoCoverModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNVideoCoverModule"/>.
        /// </summary>
        internal RNVideoCoverModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNVideoCover";
            }
        }
    }
}
