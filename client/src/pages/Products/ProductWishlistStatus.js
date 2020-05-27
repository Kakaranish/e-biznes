import React, { useState, useEffect } from 'react';
import axios from 'axios';
import wishlistOnIcon from '../../assets/img/heart-on.svg';
import wishlistOffIcon from '../../assets/img/heart-off.svg';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';
import { doRequest } from '../../Utils';

const ProductWishlistStatus = (props) => {

    const initState = props.initState;
    const productId = props.productId;

    const isWishlistedState = { wishlisted: true, icon: wishlistOnIcon };
    const isNotWishlistedState = { wishlisted: false, icon: wishlistOffIcon };
    const [wishlisted, setWishlisted] = useState(isNotWishlistedState);

    useEffect(() => {
        const fetchIsWishlisted = async () => {
            try {
                const action = async () => axios.get(`/api/wishlist/product/${productId}/status`, {
                    validateStatus: false,
                    headers: { 'X-Auth-Token': props.auth.token }
                });
                const result = await doRequest(action);
                if (!result) return;
                setWishlisted(isWishlistedState);
            } catch (error) {
                alert(`${error} error occured`);
            }
        };

        if (initState === undefined) fetchIsWishlisted();
        else {
            if (!initState) setWishlisted(isNotWishlistedState);
            else setWishlisted(isWishlistedState);
        }
    }, []);

    const toggleWishlist = async () => {

        let result;
        const headers = { 'X-Auth-Token': props.auth.token };

        let action;
        if (wishlisted.wishlisted) action = async () => axios.delete('/api/wishlist/product', {
            data: { productId: productId },
            validateStatus: false,
            headers
        });
        else action = async () => axios.post('/api/wishlist/product',
            { productId: productId }, { validateStatus: false, headers });

        try {
            const result = await doRequest(action);
            if (wishlisted.wishlisted) setWishlisted(isNotWishlistedState);
            else setWishlisted(isWishlistedState);
        } catch (error) {
            alert(`${error} error occured`);
        }
    };

    return <>
        <img src={wishlisted.icon} style={{ width: "25px", marginLeft: "10px", cursor: "pointer" }}
            onClick={toggleWishlist} title="Wishlist product" />
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(ProductWishlistStatus);