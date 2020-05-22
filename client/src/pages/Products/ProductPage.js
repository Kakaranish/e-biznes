import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { isValidUUID } from '../../common';
import ProductWishlistStatus from './ProductWishlistStatus';
import * as AuthUtils from '../Auth/Utils';

const ProductPage = (props) => {

    const productId = props.match.params.id;

    const addToCartOnClick = () => {

    }

    const [state, setState] = useState({ loading: true, result: null });
    useEffect(() => {
        const fetchProduct = async () => {
            const headers = props.auth?.token ? { 'X-Auth-Token': props.auth.token } : {};
            const result = await axios.get(`/api/products/${productId}`, {
                headers: headers, validateStatus: false
            });

            if (result.status === 404) {
                setState({ loading: false, result: null });
                return;
            }
            if (result.status !== 200) {
                alert('Some error occured');
                console.log(headers);
                return;
            }
            setState({ loading: false, result: result.data });
        };

        if (isValidUUID(productId)) fetchProduct();
    }, []);

    if (!isValidUUID(productId)) return <h3>Product Id '{productId}' is invalid UUID</h3>
    else if (state.loading) return <></>
    else {
        if (!state.result) return <h3>Product Id '{productId}' does not exist</h3>
        else return <>
            <h3>
                {state.result.product.name}
                {props.auth && <ProductWishlistStatus productId={productId}
                    initState={state.result.wishlistItem ? true : false} />}
            </h3>

            <p>
                <b>Is in cart? </b> {state.result.cartItem ? "Yes" : "No"}
            </p>

            <p>
                <b>Description:</b> {state.result.product.description}
            </p>

            <p>
                <b>Price:</b> {state.result.product.price.toFixed(2)}PLN
            </p>

            <p>
                <b>Available quantity:</b> {state.result.product.quantity}
            </p>

            <p>
                <b>Category:</b> {state.result.category?.name ?? 'None'}
            </p>

            <button type="button" className="btn btn-primary w-25 mr-2" onClick={addToCartOnClick}>
                Add to cart
			</button>
        </>
    }
};


export default AuthUtils.createAuthAwareComponent(ProductPage);