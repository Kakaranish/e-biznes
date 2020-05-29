import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import axios from 'axios';
import moment from 'moment';
import Modal from '../../components/Modal';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';
import { doRequest } from '../../Utils';

const OrderPage = (props) => {

    const orderId = props.match.params.id;
    const history = useHistory();

    const [state, setState] = useState({ loading: true, orderInfo: null });
    useEffect(() => {
        const fetchOrder = async () => {
            let result;
            try {
                const action = async () => axios.get(`/api/orders/${orderId}`, {
                    headers: { 'X-Auth-Token': props.auth.token },
                    validateStatus: false
                });
                result = await doRequest(action);
            } catch (error) {
                alert(`${error} error occured`);
                return;
            }

            let totalPrice = 0;
            result.cartItems.forEach(ci => totalPrice += ci.cartItem.quantity * ci.cartItem.pricePerProduct);

            let paymentsValue = 0;
            result.payments.forEach(payment => {
                if(payment.status === "ACCEPTED") paymentsValue += payment.amountOfMoney
            });

            let toPay = parseFloat((totalPrice - paymentsValue).toFixed(2));

            setState({
                loading: false,
                orderInfo: result,
                totalPrice: totalPrice,
                paymentsValue: paymentsValue,
                toPay: toPay
            });
        };

        fetchOrder();
    }, []);

    const deleteShippingInfoOnClick = async () => {

        try {
            const action = async () => axios.delete('/api/shipping-info', {
                data: { shippingInfoId: state.orderInfo.shippingInfo.id },
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
            });
            await doRequest(action);

            history.go();
        } catch (error) {
            alert(`${error} error occured`);
        }
    }

    if (state.loading) return <></>
    else if (!state.orderInfo) return <h3>No such order...</h3>
    return <>

        <h3>Order {orderId}</h3>
        <p>Created: {moment(state.orderInfo.order.dateCreated).format('YYYY-MM-DD hh:mm:ss')}</p>

        <p>
            <b>Paid? </b>
            {
                state.toPay <= 0
                    ? <span className="text-success">Yes</span>
                    : <span className="text-danger">No</span>
            }
        </p>

        <p>
            <b>Total Price:</b> {state.totalPrice.toFixed(2)} PLN
        </p>

        <p>
            <b>Money paid in: </b> {state.paymentsValue.toFixed(2)} PLN
        </p>

        <p>
            <b>Remaining money to paid:</b> {state.toPay} PLN
        </p>

        {
            state.toPay > 0 &&
            <Link to={`/orders/${orderId}/payment`} className="btn btn-primary w-25 mb-5">
                Pay
            </Link>
        }

        <h3 className="mt-5">Shipping info</h3>
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

        <div className="mb-5">
            <h3>Payments</h3>
            {!state.orderInfo.payments || state.orderInfo.payments.length === 0
                ? <p>You have no payments yet</p>

                :
                state.orderInfo.payments.map((p, i) =>
                    <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`p-${p.id}`}>

                        <p>
                            <b>Payment Id:</b> {p.id}
                        </p>

                        <p>
                            <b>Amount of money:</b> {p.amountOfMoney} PLN
                        </p>

                        <p>
                            <b>Payment method:</b> {p.methodCode}
                        </p>

                        <p>
                            <b>Paid in:</b> {moment(p.dateCreated).format('YYYY-MM-DD hh:mm:ss')}
                        </p>

                        <p>
                            <b>Status:</b> {p.status}
                        </p>
                    </div>
                )
            }
        </div>

        <h3>Ordered products</h3>
        {
            state.orderInfo.cartItems.map((ci, i) =>
                <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`div-${ci.product.id}`}>
                    <p>
                        <b>{ci.product.name}</b>
                    </p>

                    {
                        ci.product.description &&
                        <p>
                            <b>Description:</b> {ci.product.description}
                        </p>
                    }

                    <p>
                        <b>Quantity:</b> {ci.cartItem.quantity}
                    </p>

                    <p>
                        <b>Price/Item:</b> {ci.cartItem.pricePerProduct.toFixed(2)}PLN
                    </p>

                    <p>
                        <b>Total price:</b> {ci.cartItem.pricePerProduct.toFixed(2) * ci.cartItem.quantity} PLN
                    </p>
                </div>
            )
        }
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(OrderPage);