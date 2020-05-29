import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';
import ShippingInfo from './components/ShippingInfo';
import Payments from './components/Payments';
import OrderedProducts from '../../components/Orders/OrderedProducts';
import BasicOrderInfo from '../../components/Orders/BasicOrderInfo';
import { fetchOrderInfoAsUser } from '../../common/orders-common';

const OrderPage = (props) => {

    const orderId = props.match.params.id;

    const [state, setState] = useState({ loading: true, orderInfo: null });
    useEffect(() => {
        const fetchOrder = async () => {
            try {
                const result = await fetchOrderInfoAsUser(props.auth, orderId);
                if(result) setState(Object.assign({loading: false}, result));
            } catch (error) { alert(`${error} error occured`); }
        };
        fetchOrder();
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