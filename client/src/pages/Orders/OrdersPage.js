import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import moment from 'moment';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';
import { doRequest } from '../../Utils';

const OrdersPage = (props) => {

    const [state, setState] = useState({ loading: true, products: null });
    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const action = async () => axios.get('/api/orders/user', {
                    headers: { 'X-Auth-Token': props.auth.token },
                    validateStatus: false
                });
                const result = await doRequest(action);
                setState({ loading: false, orders: result });
            } catch (error) {
                alert(`${error} error occured`);
            }
        };

        fetchOrders();
    }, []);

    if (state.loading) return <></>
    else if (!state.orders || state.orders.length === 0) return <>
        <h3>You have no orders yet</h3>
    </>
    else return <>
        <h3>Your orders</h3>

        {
            state.orders.map((order, i) =>
                <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`div-${order.id}`}>
                    <p>
                        Order <b>{order.id}</b>
                    </p>

                    <p>
                        Date of order: {moment(order.dateCreated).format('YYYY-MM-DD hh:mm:ss')}
                    </p>

                    <Link to={`/orders/${order.id}`} className="btn btn-primary w-25">
                        Show more information
                    </Link>
                </div>
            )
        }

    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(OrdersPage);