import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import axios from 'axios';
import * as AuthUtils from '../Auth/Utils';

const CartPage = (props) => {

    const history = useHistory();

    const [state, setState] = useState({ loading: true, cartItems: null });
    useEffect(() => {
        const getCartItems = async () => {
            const result = await axios.get('/api/cart', {
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
            });
            if (result.status !== 200) {
                alert('Some error occured');
                return;
            }
            setState({ loading: false, cartItems: result.data });
        };

        getCartItems();
    }, []);

    const finalizeOnClick = async () => {
        const result = await axios.post('/api/orders', { cartId: state.cartItems[0].cartItem.cartId }, {
            headers: { 'X-Auth-Token': props.auth.token },
            validateStatus: false
        });
        if (result.status !== 200) {
            alert('Some error occured');
            console.log(result.data);
            return;
        }

        alert('Everything is ok');
        console.log(result);
    }

    const deleteFromCart = async cartItemId => {
        const result = await axios.post('/api/cart/delete', { cartItemId: cartItemId }, {
            headers: { 'X-Auth-Token': props.auth.token },
            validateStatus: false
        });
        if (result.status !== 200) {
            alert('Some error occured');
            console.log(result.data);
            return;
        }

        history.go();
    }

    if (state.loading) return <></>
    else if (!state.cartItems || state.cartItems.length === 0) return <>
        <h3>Your shopping cart is currently empty :(</h3>
    </>
    else return <>
        <div className="mb-3">
            <h3>Summary</h3>
            <p>
                <b>Items in cart:</b> {state.cartItems.length}
            </p>

            <p>
                <b>Total price: </b>
                {
                    (() => {
                        let totalPrice = 0;
                        state.cartItems.forEach(item => totalPrice += item.cartItem.quantity * item.product.price);
                        return totalPrice.toFixed(2);
                    })()
                } PLN
            </p>

            <button className="btn btn-success" onClick={finalizeOnClick}>
                Finalize order
            </button>
        </div>

        <h3>Items in your shopping cart</h3>
        {
            state.cartItems.map(ci =>
                <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`div-${ci.product.id}`}>
                    <p><b>
                        {ci.product.name}
                    </b></p>

                    <p>
                        {ci.product.description}
                    </p>

                    <p>Quantity: {ci.cartItem.quantity}</p>

                    <p>Price/Item: {ci.product.price.toFixed(2)}PLN</p>

                    <p>Total price: {ci.product.price.toFixed(2) * ci.cartItem.quantity} PLN</p>

                    <Link to={`/products/${ci.product.id}`} key={`prod-link-${ci.product.id}`}
                        className="btn btn-primary mr-2">
                        Go to product
                    </Link>

                    <button className="btn btn-danger" onClick={async () => await deleteFromCart(ci.cartItem.id)}>
                        Remove from cart
                    </button>
                </div>
            )
        }

    </>
};

export default AuthUtils.createAuthAwareComponent(CartPage);