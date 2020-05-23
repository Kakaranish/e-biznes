import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { createAuthAwareComponent } from '../Auth/Utils';
import axios from 'axios';
import moment from 'moment';
import Modal from '../../components/Modal';

const OrderPage = (props) => {
    
    const orderId = props.match.params.id;
    const history = useHistory();

    const [state, setState] = useState({ loading: true, orderInfo: null });
    useEffect(() => {
        const fetchOrder = async () => {
            const result = await axios.get(`/api/orders/${orderId}`, {
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
            });
            if (result.status !== 200) {
                alert('Some error occured');
                return;
            }

            let totalPrice = 0;
            result.data.cartItems.forEach(ci => totalPrice += ci.cartItem.quantity * ci.product.price);

            let paymentsValue = 0;
            result.data.payments.forEach(payment => paymentsValue += payment.amountOfMoney);

            setState({
                loading: false,
                orderInfo: result.data,
                totalPrice: totalPrice,
                paymentsValue: paymentsValue
            });
        };

        fetchOrder();
    }, []);

    const deleteShippingInfoOnClick = async () => {
        const result = await axios.delete('/api/shipping-info', {
            data: { shippingInfoId: state.orderInfo.shippingInfo.id },
            headers: { 'X-Auth-Token': props.auth.token },
            validateStatus: false
        })
        if (result.status !== 200) {
            alert('Some error occured');
            console.log(result);
            return;
        }
        history.go();
    }

    if (state.loading) return <></>
    else if (!state.orderInfo) return <h3>No such order...</h3>
    return <>

        <h3>Order {orderId}</h3>
        <p>Created: {moment(state.orderInfo.order.dateCreated).format('YYYY-MM-DD hh:mm:ss')}</p>

        <p>
            <b>Paid? </b>
            {
                state.paymentsValue < state.totalPrice
                    ? <span className="text-danger">No</span>
                    : <span className="text-success">Yes</span>
            }
        </p>

        <p>
            <b>Total Price:</b> {state.totalPrice.toFixed(2)} PLN
        </p>

        <p>
            <b>Money paid in: </b> {state.paymentsValue.toFixed(2)} PLN
        </p>

        <p>
            <b>Remaining money to paid:</b> {(state.totalPrice - state.paymentsValue).toFixed(2)} PLN
        </p>

        {
            state.paymentsValue < state.totalPrice &&
            <Link to={'/'} className="btn btn-primary w-25 mb-5">
                Pay
            </Link>
        }

        <h3>Shipping info</h3>
        <div className="p-3 mb-5" style={{ border: "1px solid gray" }}>

            {
                !state.orderInfo.shippingInfo
                    ? <p> You provided no shipping info </p>

                    : <>
                        <p>
                            <b>Country:</b> {state.orderInfo.shippingInfo.country}
                        </p>

                        <p>
                            <b>City:</b> {state.orderInfo.shippingInfo.city}
                        </p>

                        <p>
                            <b>Address:</b> {state.orderInfo.shippingInfo.address}
                        </p>

                        <p>
                            <b>Zip/Postcode:</b> {state.orderInfo.shippingInfo.zipOrPostcode}
                        </p>
                    </>
            }

            <div>
                <Link to={`/orders/${orderId}/shipping-info`} className="btn btn-primary w-25">
                    {
                        !state.orderInfo.shippingInfo
                            ? "Add shipping info"
                            : "Edit"
                    }
                </Link>

                {
                    state.orderInfo.shippingInfo &&
                    <Modal title={"Are you sure?"}
                        btnText={"Delete"}
                        btnClasses={"btn btn-danger w-25"}
                        modalTitle={"Are you sure?"}
                        modalPrimaryBtnText={"Delete"}
                        modalPrimaryBtnClasses={"btn btn-danger"}
                        onModalPrimaryBtnClick={deleteShippingInfoOnClick}
                        modalSecondaryBtnText={"Cancel"}
                        modalSecondaryBtnClasses={"btn btn-secondary"}
                    />
                }
            </div>
        </div>

        <h3>Payments</h3>
        <div className="p-3 mb-5" style={{ border: "1px solid gray" }}>
            {
                !state.orderInfo.payments || state.orderInfo.payments.length === 0
                    ? <p>You have no payments yet</p>
                    : <> Payments info </>
            }
        </div>

        <h3>Ordered products</h3>
        {
            state.orderInfo.cartItems.map((ci, i) =>
                <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`div-${ci.product.id}`}>
                    <p>
                        <b>{ci.product.name}</b>
                    </p>

                    <p>
                        {ci.product.description}
                    </p>

                    <p>Quantity: {ci.cartItem.quantity}</p>

                    <p>Price/Item: {ci.product.price.toFixed(2)}PLN</p>

                    <p>Total price: {ci.product.price.toFixed(2) * ci.cartItem.quantity} PLN</p>
                </div>
            )
        }
    </>
};

export default createAuthAwareComponent(OrderPage);