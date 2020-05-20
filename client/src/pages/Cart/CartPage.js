import React, { useState, useEffect } from 'react';
import axios from 'axios';

const CartPage = () => {


    const [state, setState] = useState({ loading: true, cartItems: null });
    useEffect(() => {
        const getMessage = async () => {
            const result = await axios.get('/api/cart', {
                headers: { 'X-Auth-Token': localStorage.getItem('token') },
                validateStatus: false
            });
            if (result.status !== 200) {
                alert('Some error occured');
                return;
            }
            setState({ loading: false, cartItems: result.data });
        };

        getMessage();
    }, []);

    if (state.loading) return <></>
    else if (!state.cartItems || state.cartItems.length === 0) return <>
        <h3>Your shopping cart is currently empty :(</h3>
    </>
    else return <>
        <h3>Items in your shopping cart</h3>

        {
            state.cartItems.map(ci =>
                <div className="p-2" style={{ border: "1px solid gray" }} key={`div-${ci.product.id}`}>
                    <p><b>
                        {ci.product.name}
                    </b></p>

                    <p>
                        {ci.product.description}
                    </p>

                    <p>Quantity: {ci.cartItem.quantity}</p>

                    <p>Price/Item: {ci.product.price.toFixed(2)}PLN</p>

                    <p>Total price: {ci.product.price.toFixed(2) * ci.cartItem.quantity}</p>
                </div>
            )
        }
    </>
};

export default CartPage;