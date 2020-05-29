import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';
import ShippingInfo from './components/ShippingInfo';
import Payments from './components/Payments';
import OrderedProducts from '../../components/Orders/OrderedProducts';
import { fetchOrder } from '../../common/orders-common';
import BasicOrderInfo from '../../components/Orders/BasicOrderInfo';

const OrderPage = (props) => {

    const orderId = props.match.params.id;

    const [state, setState] = useState({ loading: true, orderInfo: null });
    useEffect(() => {
        const fetch = async () => {
            const result = await fetchOrder(props.auth, orderId);
            setState(result, { loading: false });
        }
        fetch();
    }, []);


    if (state.loading) return <></>
    else if (!state.orderInfo) return <h3>No such order...</h3>
    return <>
        <BasicOrderInfo orderState={state} showUser={false} />

        {
            state.toPay > 0 &&
            <Link to={`/orders/${orderId}/payment`} className="btn btn-primary w-25 mb-5">
                Pay
            </Link>
        }

        <ShippingInfo orderId={orderId} shippingInfo={state.orderInfo.shippingInfo} />

        <Payments payments={state.orderInfo.payments} />

        <OrderedProducts cartItems={state.orderInfo.cartItems} />
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(OrderPage);