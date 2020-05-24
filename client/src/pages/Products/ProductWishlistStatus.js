import React, { useState, useEffect } from 'react';
import axios from 'axios';
import * as Utils from '../../Utils';
import wishlistOnIcon from '../../assets/img/heart-on.svg';
import wishlistOffIcon from '../../assets/img/heart-off.svg';

const ProductWishlistStatus = (props) => {

    const initState = props.initState;
    const productId = props.productId;

    const isWishlistedState = { wishlisted: true, icon: wishlistOnIcon };
    const isNotWishlistedState = { wishlisted: false, icon: wishlistOffIcon };
    const [wishlisted, setWishlisted] = useState(isNotWishlistedState);

    useEffect(() => {
        const fetchIsWishlisted = async () => {
            const result = await axios.get(`/api/wishlist/product/${productId}/status`, {
                validateStatus: false,
                headers: { 'X-Auth-Token': props.auth.token }
            });
            if (result.status !== 200) {
                alert('Some error occured');
                return;
            }

            if (!result.data) return;
            setWishlisted(isWishlistedState);
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
        if (wishlisted.wishlisted) {
            result = await axios.delete('/api/wishlist/product', {
                data: { productId: productId },
                validateStatus: false,
                headers
            });
        } else {
            result = await axios.post('/api/wishlist/product',
                { productId: productId }, { validateStatus: false, headers });
        }

        if (result.status !== 200) {
            alert("Error while adding to wishlist occured. Try to refresh page.");
            return;
        }
        if (wishlisted.wishlisted) setWishlisted(isNotWishlistedState);
        else setWishlisted(isWishlistedState);
    };

    return <>
        <img src={wishlisted.icon} style={{ width: "25px", marginLeft: "10px", cursor: "pointer" }}
            onClick={toggleWishlist} title="Wishlist product" />
    </>
};

export default Utils.createAuthAwareComponent(ProductWishlistStatus);