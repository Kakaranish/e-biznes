import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const ProductsByCategoryPage = (props) => {

    const categoryId = props.match.params.id;

    const [state, setState] = useState({ loading: true, products: null });
    useEffect(() => {
        const fetchProducts = async () => {
            const result = await axios.get(`/api/products/category/${categoryId}`,
                { validateStatus: false });
            if (result.status !== 200) {
                setState({ loading: false, products: null });
                alert('Some error occured');
                console.log(result);
                return;
            }
            setState({ loading: false, products: result.data });
        };
        fetchProducts();
    }, []);

    if (state.loading) return <></>
    else {
        if (!state.products || !state.products.length)
            return <h3>There are no products in category {categoryId}</h3>
        else return <>
            <h3>Products in category {categoryId}</h3>
            {
                state.products.map((product, i) =>
                    <div className="p-3" style={{ border: "1px solid gray" }} key={`div-${product.id}`}>
                        <p><b>{product.name}</b></p>

                        <p>Price: {product.price.toFixed(2)}PLN</p>

                        <Link to={`/products/${product.id}`} key={`link-${i}`} className="btn btn-success w-25">
                            Go to product
                        </Link>
                    </div>
                )
            }
        </>
    }
};

export default ProductsByCategoryPage;