import React from 'react';
import { Link, useHistory } from 'react-router-dom';
import axios from 'axios';
import Modal from '../../../components/Modal';
import { doRequest } from '../../../common/Utils';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';

const ShippingInfo = (props) => {

    const shippingInfo = props.shippingInfo;
    const orderId = props.orderId;
    const history = useHistory();

    const deleteShippingInfoOnClick = async () => {
        try {
            const action = async () => axios.delete('/api/shipping-info', {
                data: { shippingInfoId: shippingInfo.id },
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
            });
            await doRequest(action);

            history.go();
        } catch (error) {
            alert(`${error} error occured`);
        }
    }

    return <>
        <h3 className="mt-5">Shipping info</h3>
        <div className="p-3 mb-5" style={{ border: "1px solid gray" }}>

            {
                !shippingInfo
                    ? <p> You provided no shipping info </p>

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

            <div>
                <Link to={`/orders/${orderId}/shipping-info`} className="btn btn-primary px-3 mr-2">
                    {
                        !shippingInfo
                            ? "Add shipping info"
                            : "Edit"
                    }
                </Link>

                {
                    shippingInfo &&
                    <Modal title={"Are you sure?"}
                        btnText={"Delete"}
                        btnClasses={"btn btn-danger"}
                        modalTitle={"Are you sure?"}
                        modalPrimaryBtnText={"Delete"}
                        modalPrimaryBtnClasses={"btn btn-danger px-3"}
                        onModalPrimaryBtnClick={deleteShippingInfoOnClick}
                        modalSecondaryBtnText={"Cancel"}
                        modalSecondaryBtnClasses={"btn btn-secondary"}
                    />
                }
            </div>
        </div>
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(ShippingInfo);