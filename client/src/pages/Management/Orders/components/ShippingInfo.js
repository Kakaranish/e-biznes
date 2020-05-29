import React from 'react';

const ShippingInfo = ({ shippingInfo }) => {

    return <>
        <h3 className="mt-5">Shipping info</h3>
        <div className="p-3 mb-5" style={{ border: "1px solid gray" }}>
            {
                !shippingInfo
                    ? <p> No shipping info </p>

                    : <>
                        <p>
                            <b>Country:</b> {shippingInfo.country}
                        </p>

                        <p>
                            <b>City:</b> {shippingInfo.city}
                        </p>

                        <p>
                            <b>Address:</b> {shippingInfo.address}
                        </p>

                        <p>
                            <b>Zip/Postcode:</b> {shippingInfo.zipOrPostcode}
                        </p>
                    </>
            }
        </div>
    </>
};

export default ShippingInfo;