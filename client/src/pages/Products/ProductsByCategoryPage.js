import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const ProductsByCategoryPage = (props) => {

    const categoryId = props.match.params.id;

    const [state, setState] = useState({ loading: true, result: null });
    useEffect(() => {
        const fetchProducts = async () => {
            const result = await axios.get(`/api/products/category/${categoryId}`,
                { validateStatus: false });
            if (result.status !== 200) {
                setState({ loading: false, result: null });
                alert('Some error occured');
                console.log(result);
                return;
            }
            setState({ loading: false, result: result.data });
        };
        fetchProducts();
    }, []);

    if (state.loading) return <></>
    else {
        if (!state.result || !state.result.products?.length)
            return <h3>There are no products in category {state.result.category.name}</h3>
        else return <>
            <h3>Products in category {state.result.category.name}</h3>
            {
                state.result.products.map((item, i) =>
                    <div className="p-3" style={{ border: "1px solid gray" }} key={`div-${item.id}`}>
                        <p><b>{item.name}</b></p>

                        <p>Price: {item.price.toFixed(2)}PLN</p>

                        <Link to={`/products/${item.id}`} key={`link-${i}`} className="btn btn-success w-25">
                            Go to product
                        </Link>
                    </div>
                )
            }
        </>
    }
};

export default ProductsByCategoryPage;