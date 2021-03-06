import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import { getFormDataJsonFromEvent, doRequest } from '../../common/Utils';
import axios from 'axios';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';

const ShippingInfoPage = (props) => {

    const history = useHistory();

    const orderId = (/^\/orders\/(.*)\/shipping-info$/g).exec(props.location.pathname)[1];
    const [state, setState] = useState({ loading: true, orderInfo: null });
    useEffect(() => {
        const fetchOrder = async () => {

            try {
                const action = async () => axios.get(`/api/orders/${orderId}/shipping-info`, {
                    headers: { 'X-Auth-Token': props.auth.token },
                    validateStatus: false
                });
                const result = await doRequest(action);
                setState({ loading: false, orderInfo: result });
            } catch (error) {
                if (error === 404) {
                    setState({ loading: false, orderInfo: null });
                    return;
                }
                alert(`${error} error occured`);
            }
        }
        fetchOrder();
    }, []);

    if (state.loading) return <></>
    else if (!state.orderInfo) return <h3>There is no order with id {orderId}</h3>
    else if (state.orderInfo.shippingInfo) {

        const editOnClick = async event => {
            event.preventDefault();
            let formData = getFormDataJsonFromEvent(event);
            formData.orderId = orderId;

            const action = async () => axios.put(`/api/shipping-info`, formData, {
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
            });

            try {
                await doRequest(action);
                history.push(`/orders/${orderId}`);
            } catch (error) {
                alert(`${error} error occured`);
            }
        }

        return <>
            <h3>Edit Shipping Info</h3>
            <form onSubmit={editOnClick}>
                <input name="id" defaultValue={state.orderInfo.shippingInfo.id} hidden />

                <div className="form-group">
                    <label>Country</label>
                    <input name="country" type="text" className="form-control"
                        defaultValue={state.orderInfo.shippingInfo.country}
                        placeholder="Country..." required />
                </div>

                <div className="form-group">
                    <label>City</label>
                    <input name="city" type="text" className="form-control"
                        defaultValue={state.orderInfo.shippingInfo.city}
                        placeholder="City..." required />
                </div>

                <div className="form-group">
                    <label>Address</label>
                    <input name="address" type="text" className="form-control"
                        defaultValue={state.orderInfo.shippingInfo.address}
                        placeholder="Address..." required />
                </div>

                <div className="form-group">
                    <label>Zip/Postcode</label>
                    <input name="zipOrPostcode" type="text" className="form-control"
                        defaultValue={state.orderInfo.shippingInfo.zipOrPostcode}
                        placeholder="Zip or postcode..." required />
                </div>

                <button type="submit" className="btn btn-success w-25">
                    Edit shipping info
                </button>
            </form>
        </>
    } else {
        const createOnClick = async event => {
            event.preventDefault();
            let formData = getFormDataJsonFromEvent(event);
            formData.orderId = orderId;

            try {
                const action = async () => axios.post(`/api/shipping-info`, formData, {
                    headers: { 'X-Auth-Token': props.auth.token },
                    validateStatus: false
                });
                await doRequest(action);
                history.push(`/orders/${orderId}`);
            } catch (error) {
                alert(`${error} error occured`);
            }
        }

        return <>
            <form onSubmit={createOnClick}>
                <div className="form-group">
                    <label>Country</label>
                    <input name="country" type="text" className="form-control" placeholder="Country..." required />
                </div>

                <div className="form-group">
                    <label>City</label>
                    <input name="city" type="text" className="form-control" placeholder="City..." required />
                </div>

                <div className="form-group">
                    <label>Address</label>
                    <input name="address" type="text" className="form-control" placeholder="Address..." required />
                </div>

                <div className="form-group">
                    <label>Zip/Postcode</label>
                    <input name="zipOrPostcode" type="text" className="form-control" placeholder="Zip or postcode..." required />
                </div>

                <button type="submit" className="btn btn-success w-25">
                    Create shipping info
                </button>
            </form>
        </>
    }
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(ShippingInfoPage);