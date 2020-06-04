import React from 'react';

const CategoryForm = ({ initState, onSubmitCb, validationErrors }) => <>
    <form onSubmit={onSubmitCb}>

        {
            initState &&
            <div className="form-group">
                <input name="id" type="text" className="form-control" value={initState.categoryId} readOnly />
            </div>
        }

        <div className="form-group">
            <input name="name" type="text" className="form-control" defaultValue={initState?.categoryName} />
        </div>

        <button type="submit" className="btn btn-success w-25">
            Submit
        </button>

        {
            validationErrors &&
            <div className="col-12 mt-2">
                <p className="text-danger font-weight-bold" style={{ marginBottom: '0px' }}>
                    Validation errors
                </p>

                <ul style={{ paddingTop: "0", marginTop: "0px" }}>
                    {
                        validationErrors.map((error, i) => {
                            return <li key={`val-err-${i}`} className="text-danger">{error}</li>
                        })
                    }
                </ul>
            </div>
        }

    </form>
</>

export default CategoryForm;